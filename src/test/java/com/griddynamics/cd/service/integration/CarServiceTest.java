package com.griddynamics.cd.service.integration;

import com.griddynamics.cd.BaseIntegrationTest;
import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import com.griddynamics.cd.service.CarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CarServiceTest extends BaseIntegrationTest {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CarService carService;
    private final List<Car> cars = List.of(
            Car.builder()
                    .id(1L)
                    .manufacturer("Honda")
                    .model("Coupe")
                    .vinNumber("4T3ZK3BB7BU042861")
                    .color(Color.BLACK)
                    .employeeId(1L)
                    .build(),
            Car.builder()
                    .id(2L)
                    .manufacturer("Nissan")
                    .model("Silvia S13")
                    .color(Color.GREY)
                    .employeeId(2L)
                    .build(),
            Car.builder()
                    .id(3L)
                    .manufacturer("Toyota")
                    .model("Chaser")
                    .vinNumber("1HGCG2254WA015540")
                    .color(Color.WHITE)
                    .employeeId(2L)
                    .build(),
            Car.builder()
                    .id(4L)
                    .manufacturer("Toyota")
                    .model("Mark 2")
                    .vinNumber("4T3ZK3BB7BU042861")
                    .color(Color.BLACK)
                    .employeeId(2L)
                    .build()

    );

    @BeforeEach
    void setUp() {
        EmployeeEntity employeeEntity1 = EmployeeEntity.builder()
                .firstName("Alfred")
                .lastName("Miles")
                .address("Atlanta, Georgia US.")
                .birthday(LocalDate.of(1995, 6, 21))
                .phoneNumber("4539832543")
                .build();
        EmployeeEntity employeeEntity2 = EmployeeEntity.builder()
                .firstName("Darius")
                .lastName("Epps")
                .address("Abuja, Nigeria")
                .birthday(LocalDate.of(1993, 8, 2))
                .phoneNumber("5738310041")
                .build();

        employeeRepository.saveAll(List.of(employeeEntity1, employeeEntity2));

        CarEntity carEntity1 = CarEntity.builder()
                .manufacturer("Honda")
                .model("Coupe")
                .vinNumber("4T3ZK3BB7BU042861")
                .color(Color.BLACK)
                .employee(employeeEntity1)
                .build();
        CarEntity carEntity2 = CarEntity.builder()
                .manufacturer("Nissan")
                .model("Silvia S13")
                .color(Color.GREY)
                .employee(employeeEntity2)
                .build();
        CarEntity carEntity3 = CarEntity.builder()
                .manufacturer("Toyota")
                .model("Chaser")
                .vinNumber("1HGCG2254WA015540")
                .color(Color.WHITE)
                .employee(employeeEntity2)
                .build();
        CarEntity carEntity4 = CarEntity.builder()
                .manufacturer("Toyota")
                .model("Mark 2")
                .vinNumber("4T3ZK3BB7BU042861")
                .color(Color.BLACK)
                .employee(employeeEntity2)
                .build();

        carRepository.saveAll(List.of(carEntity1, carEntity2, carEntity3, carEntity4));
    }

    @AfterEach
    void cleanUp() throws SQLException {
        Statement st = connection.createStatement();

        st.execute("TRUNCATE TABLE car, employee RESTART IDENTITY;");
        st.close();
    }

    @Test
    void getAllCars_whenSaveToCarRepository_thenReturnValidList() {
        Map<String, Object> values = new HashMap<>();
        values.put("pageNumber", 0);
        values.put("pageSize", 3);
        values.put("totalPages", 1);
        values.put("totalObjects", 3L);
        values.put("cars", List.of(cars.get(2), cars.get(0), cars.get(3)));

        ResponseEntity<Map<String, Object>> expected = ResponseEntity.ok(values);
        ResponseEntity<Map<String, Object>> actual = carService.getCarsWithFiltering(
                null, null, null, null, null,
                0, 3, "model", Sort.Direction.ASC);

        assertEquals(expected, actual);
    }

    @Test
    void getCarById_whenPassValidIdTwoTimes_thenReturnValidModel() {
        assertEquals(cars.get(1), carService.getCarById(2L));
        assertEquals(cars.get(3), carService.getCarById(4L));
    }

    @Test
    void getCarById_whenPassInvalidCarId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.getCarById(123L)
        );
        assertEquals("Car with 123 id was not found", thrown.getMessage());
    }

    @Test
    void getCarsByEmployeeId_whenCallMethodTwoTimes_thenReturnValidListOfEmployeeModels() {
        assertEquals(List.of(cars.get(0)), carService.getCarsByEmployeeId(1L));
        assertEquals(List.of(cars.get(1), cars.get(2), cars.get(3)), carService.getCarsByEmployeeId(2L));
    }

    @Test
    void saveCar_whenPassValidCreateCarRequest_thenReturnValidModel() {
        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .manufacturer("Nissan")
                .model("Silvia S13")
                .employeeId(1L)
                .color(Color.GREY)
                .build();
        Car expected = Car.builder()
                .id(5L)
                .manufacturer("Nissan")
                .model("Silvia S13")
                .employeeId(1L)
                .color(Color.GREY)
                .build();

        assertEquals(expected, carService.saveCar(createCarRequest));
    }

    @Test
    void saveCar_whenPassCreateCarRequestWithInvalidEmployeeId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.saveCar(CreateCarRequest.builder()
                        .employeeId(123L)
                        .build())
        );
        assertEquals("Employee with 123 id was not found", thrown.getMessage());
    }

    @Test
    void updateCar_whenPassValidUpdateCarRequest_thenReturnValidModel() {
        UpdateCarRequest updateCarRequest = UpdateCarRequest.builder()
                .manufacturer("Audi")
                .model("A2")
                .vinNumber("JH4KA8271NC000480")
                .employeeId(1L)
                .color(Color.WHITE)
                .build();

        Car expected = Car.builder()
                .id(3L)
                .manufacturer("Audi")
                .model("A2")
                .vinNumber("JH4KA8271NC000480")
                .employeeId(1L)
                .color(Color.WHITE)
                .build();

        assertEquals(expected, carService.updateCar(updateCarRequest, 3L));
    }

    @Test
    void updateCar_whenPassInvalidCarId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.updateCar(new UpdateCarRequest(), 123L)
        );
        assertEquals("Car with 123 id was not found", thrown.getMessage());
    }

    @Test
    void updateCar_whenPassUpdateCarRequestWithInvalidEmployeeId_thenThrowEntityNotFoundException() {
        UpdateCarRequest updateCarRequest = UpdateCarRequest.builder()
                .employeeId(54L)
                .build();

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.updateCar(updateCarRequest, 3L)
        );
        assertEquals("Employee with 54 id was not found", thrown.getMessage());
    }

    @Test
    void deleteCar_whenPassValidCarId_thenCheckIfEntityActuallyDeleted() {
        carService.deleteCar(1L);
        assertFalse(carRepository.existsById(27L));
    }

    @Test
    void deleteCar_whenPassInvalidCarId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.deleteCar(12L)
        );
        assertEquals("Car with 12 id was not found", thrown.getMessage());
    }
}
