package com.griddynamics.cd.service.unit;

import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.mapper.CarMapper;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import com.griddynamics.cd.service.CarService;
import org.hibernate.jpa.TypedParameterValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CarServiceTest {

    private CarRepository carRepository;
    private EmployeeRepository employeeRepository;
    private CarMapper carMapper;
    private CarService carService;

    @BeforeEach
    void init() {
        carRepository = mock(CarRepository.class);
        employeeRepository = mock(EmployeeRepository.class);
        carMapper = mock(CarMapper.class);
        carService = new CarService(carRepository, employeeRepository, carMapper);
    }

    @BeforeEach
    void setUp() {
        when(carRepository.save(any(CarEntity.class)))
                .thenReturn(new CarEntity());

        when(carRepository.findById(anyLong()))
                .thenReturn(Optional.of(new CarEntity()));

        when(carMapper.toCarEntity(any(CreateCarRequest.class)))
                .thenReturn(new CarEntity());

        when(carMapper.toCarModel(any(CarEntity.class)))
                .thenReturn(new Car());

        when(carMapper.toCarEntity(any(UpdateCarRequest.class), any(CarEntity.class)))
                .thenReturn(new CarEntity());

        when(employeeRepository.findById(anyLong()))
                .thenReturn(Optional.of(new EmployeeEntity()));
    }

    @Test
    void getAllCars_whenCallMethod_thenValidMethodCallsNumber() {

        when(carRepository.findAllByFilterParamsAndSortAndPaged(
                any(TypedParameterValue.class),
                any(TypedParameterValue.class),
                any(TypedParameterValue.class),
                any(TypedParameterValue.class),
                any(TypedParameterValue.class),
                any(Pageable.class)
        ))
                .thenReturn(List.of(new CarEntity(), new CarEntity(), new CarEntity()));

        when(carRepository.existsByColumnName(eq("model")))
                .thenReturn(true);

        carService.getCarsWithFiltering(null, null, null,
                null, null, 0, 4,
                "model", Sort.Direction.ASC);

        verify(carRepository, times(1)).findAllByFilterParamsAndSortAndPaged(
                any(TypedParameterValue.class),
                any(TypedParameterValue.class),
                any(TypedParameterValue.class),
                any(TypedParameterValue.class),
                any(TypedParameterValue.class),
                any(Pageable.class)
        );
        verify(carRepository, times(1)).existsByColumnName(eq("model"));
        verify(carMapper, times(3)).toCarModel(any(CarEntity.class));
    }

    @Test
    void getCarById_whenPassCarId_thenValidMethodCallsNumber() {
        carService.getCarById(1L);
        carService.getCarById(2L);
        carService.getCarById(1L);

        verify(carRepository, times(2)).findById(1L);
        verify(carRepository, times(1)).findById(2L);
        verify(carMapper, times(3)).toCarModel(any(CarEntity.class));
    }

    @Test
    void getCarById_whenPassInvalidCarId_thenThrowEntityNotFoundException() {
        when(carRepository.findById(100L))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.getCarById(100L)
        );
        assertEquals(thrown.getMessage(), "Car with 100 id was not found");
    }

    @Test
    void getCarByEmployeeId_whenPassEmployeeId_thenValidMethodCallsNumber() {
        when(carRepository.findAllCarsByEmployeeId(1L))
                .thenReturn(List.of(new CarEntity(), new CarEntity()));

        carService.getCarsByEmployeeId(1L);

        verify(carRepository, times(1)).findAllCarsByEmployeeId(1L);
        verify(carMapper, times(2)).toCarModel(any(CarEntity.class));
    }

    @Test
    void saveCar_whenPassCreateCarRequestWithoutEmployeeId_thenValidMethodCallsNumber() {
        CreateCarRequest createCarRequest = mock(CreateCarRequest.class);

        carService.saveCar(createCarRequest);

        verify(carMapper, times(1)).toCarModel(any(CarEntity.class));
        verify(carMapper, times(1)).toCarEntity(any(CreateCarRequest.class));
        verify(carRepository, times(1)).save(any(CarEntity.class));
    }

    @Test
    void saveCar_whenPassCreateCarRequestWithEmployeeId_thenValidMethodCallsNumber() {
        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .employeeId(1L)
                .build();

        carService.saveCar(createCarRequest);

        verify(employeeRepository, times(1)).findById(1L);
        verify(carMapper, times(1)).toCarModel(any(CarEntity.class));
        verify(carMapper, times(1)).toCarEntity(any(CreateCarRequest.class));
        verify(carRepository, times(1)).save(any(CarEntity.class));
    }

    @Test
    void saveCar_whenPassDifferentCreateCarRequests_thenValidMethodCallsNumber() {
        CreateCarRequest createCarRequest1 = new CreateCarRequest();
        CreateCarRequest createCarRequest2 = CreateCarRequest.builder()
                .employeeId(5L)
                .build();

        carService.saveCar(createCarRequest1);
        carService.saveCar(createCarRequest2);

        verify(employeeRepository, times(1)).findById(5L);
        verify(carMapper, times(2)).toCarModel(any(CarEntity.class));
        verify(carMapper, times(2)).toCarEntity(any(CreateCarRequest.class));
        verify(carRepository, times(2)).save(any(CarEntity.class));
    }

    @Test
    void saveCar_whenPassInvalidEmployeeId_thenThrowEntityNotFoundException() {
        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .employeeId(10L)
                .build();

        when(employeeRepository.findById(10L))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.saveCar(createCarRequest)
        );
        assertEquals("Employee with 10 id was not found", thrown.getMessage());
    }

    @Test
    void updateCar_whenUpdateCarRequestWithoutEmployeeId_thenValidMethodCallsNumber() {
        UpdateCarRequest updateCarRequest = new UpdateCarRequest();

        carService.updateCar(updateCarRequest, 1L);

        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(any(CarEntity.class));
        verify(carMapper, times(1)).toCarModel(any(CarEntity.class));
        verify(carMapper, times(1)).toCarEntity(eq(updateCarRequest), any(CarEntity.class));
    }

    @Test
    void updateCar_whenUpdateCarRequestWithEmployeeId_thenValidMethodCallsNumber() {
        UpdateCarRequest updateCarRequest = UpdateCarRequest.builder()
                .employeeId(1L)
                .build();

        carService.updateCar(updateCarRequest, 3L);

        verify(employeeRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).findById(3L);
        verify(carRepository, times(1)).save(any(CarEntity.class));
        verify(carMapper, times(1)).toCarModel(any(CarEntity.class));
        verify(carMapper, times(1)).toCarEntity(any(UpdateCarRequest.class), any(CarEntity.class));
    }

    @Test
    void updateCar_whenPassInvalidCarId_thenThrowEntityNotFoundException() {
        when(carRepository.findById(123L))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.updateCar(new UpdateCarRequest(), 123L)
        );
        assertEquals(thrown.getMessage(), "Car with 123 id was not found");
    }

    @Test
    void updateCar_whenPassUpdateCarRequestWithInvalidEmployeeId_thenThrowEntityNotFoundException() {
        UpdateCarRequest updateCarRequest = UpdateCarRequest.builder()
                .employeeId(12L)
                .build();

        when(employeeRepository.findById(12L))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.updateCar(updateCarRequest, 123L)
        );
        assertEquals("Employee with 12 id was not found", thrown.getMessage());
    }

    @Test
    void deleteCar_whenDeleteById_thenPassValidValue() {
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        doNothing().when(carRepository)
                .deleteById(captor.capture());

        when(carRepository.existsById(1L))
                .thenReturn(true);

        carService.deleteCar(1L);

        verify(carRepository, times(1)).deleteById(anyLong());
        assertEquals(1L, captor.getValue());
    }

    @Test
    void deleteCar_whenPassInvalidCarId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.deleteCar(15L)
        );
        assertEquals("Car with 15 id was not found", thrown.getMessage());
    }
}
