package com.griddynamics.cd.service;

import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.exception.ColumnNotFoundException;
import com.griddynamics.cd.mapper.CarMapper;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import com.vladmihalcea.hibernate.type.array.LongArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.AllArgsConstructor;
import org.hibernate.jpa.TypedParameterValue;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final EmployeeRepository employeeRepository;
    private final CarMapper carMapper;

    public List<Car> getAllCars() {
        return carRepository.findAll().stream()
                .map(carMapper::toCarModel)
                .toList();
    }

    public ResponseEntity<?> getCarsWithFiltering(List<String> manufacturers,
                                                  List<String> models,
                                                  List<String> vinNumbers,
                                                  List<Long> employeeIds,
                                                  List<Color> colors,
                                                  int pageNumber,
                                                  int pageSize,
                                                  String orderBy,
                                                  Sort.Direction order) {
        if (!carRepository.existsByColumnName(orderBy)) {
            throw new ColumnNotFoundException(orderBy);
        }

        Page<Car> page = new PageImpl<>(carRepository.findAllByFilterParamsAndSortAndPaged(
                        new TypedParameterValue(StringArrayType.INSTANCE,
                                Optional.ofNullable(manufacturers).map(list -> list.toArray(String[]::new)).orElse(null)),
                        new TypedParameterValue(StringArrayType.INSTANCE,
                                Optional.ofNullable(models).map(list -> list.toArray(String[]::new)).orElse(null)),
                        new TypedParameterValue(StringArrayType.INSTANCE,
                                Optional.ofNullable(vinNumbers).map(list -> list.toArray(String[]::new)).orElse(null)),
                        new TypedParameterValue(LongArrayType.INSTANCE,
                                Optional.ofNullable(employeeIds).map(list -> list.toArray(Long[]::new)).orElse(null)),
                        new TypedParameterValue(StringArrayType.INSTANCE,
                                Optional.ofNullable(colors).map(list -> list.stream()
                                                .map(Color::name)
                                                .toArray(String[]::new))
                                        .orElse(null)),
                        PageRequest.of(pageNumber, pageSize, Sort.by(order, orderBy))).stream()
                .map(carMapper::toCarModel)
                .toList()
        );

        if (page.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        HashMap<String, Object> values = new HashMap<>();
        values.put("pageNumber", page.getNumber());
        values.put("pageSize", page.getSize());
        values.put("totalPages", page.getTotalPages());
        values.put("totalObjects", page.getTotalElements());
        values.put("cars", page.getContent());

        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    public Car getCarById(Long carId) {
        return carMapper.toCarModel(
                carRepository.findById(carId)
                        .orElseThrow(() -> new EntityNotFoundException("Car with " + carId + " id was not found"))
        );
    }

    public List<Car> getCarsByEmployeeId(Long employeeId) {
        return carRepository.findAllCarsByEmployeeId(employeeId).stream()
                .map(carMapper::toCarModel)
                .collect(Collectors.toList());
    }

    public Car saveCar(CreateCarRequest createCarRequest) {
        CarEntity carEntity = carMapper.toCarEntity(createCarRequest);

        if (createCarRequest.getEmployeeId() != null) {
            EmployeeEntity employeeEntity = employeeRepository.findById(createCarRequest.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee with " + createCarRequest.getEmployeeId() + " id was not found"));
            carEntity.setEmployee(employeeEntity);
        }

        return carMapper.toCarModel(carRepository.save(carEntity));
    }

    public Car updateCar(UpdateCarRequest updateCarRequest, Long carId) {
        CarEntity carEntity = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car with " + carId + " id was not found"));

        if (updateCarRequest.getEmployeeId() != null) {
            EmployeeEntity employeeEntity = employeeRepository.findById(updateCarRequest.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee with " + updateCarRequest.getEmployeeId() + " id was not found"));
            carEntity.setEmployee(employeeEntity);
        }

        return carMapper.toCarModel(
                carRepository.save(
                        carMapper.toCarEntity(updateCarRequest, carEntity)
                )
        );
    }

    public void deleteCar(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new EntityNotFoundException("Car with " + carId + " id was not found");
        }
        carRepository.deleteById(carId);
    }
}
