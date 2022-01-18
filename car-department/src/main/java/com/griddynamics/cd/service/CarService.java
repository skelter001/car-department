package com.griddynamics.cd.service;

import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.mapper.CarMapper;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
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
                .collect(Collectors.toList());
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
