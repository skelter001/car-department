package com.griddynamics.cd.service;

import com.griddynamics.cd.mapper.CarMapper;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.CarRequest;
import com.griddynamics.cd.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public List<Car> getAllCars() {
        return StreamSupport.stream(carRepository.findAll().spliterator(), false)
                .map(carMapper::toCarModel)
                .collect(Collectors.toList());
    }

    public Car getCarById(Long carId) {
        return carMapper.toCarModel(
                carRepository.findById(carId)
                        .orElseThrow(() -> new EntityNotFoundException("Car with " + carId + " id was not found"))
        );
    }

    public List<Car> getAllCarsByEmployeeId(Long employeeId) {
        return carRepository.findAllCarsByEmployeeId(employeeId).stream()
                .map(carMapper::toCarModel)
                .collect(Collectors.toList());
    }

    public Car saveCar(CarRequest car) {
        return carMapper.toCarModel(
                carRepository.save(
                        carMapper.toCarEntity(car))
        );
    }

    public void deleteCar(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new EntityExistsException("Car with " + carId + " id does not exist");
        }
        carRepository.deleteById(carId);
    }
}
