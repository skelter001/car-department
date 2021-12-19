package com.griddynamics.cd.service;

import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.exception.NoCarWithSuchIdException;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CarService {

    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return StreamSupport.stream(carRepository.findAll().spliterator(), false)
                .map(this::mapCarEntityToCarModel)
                .collect(Collectors.toList());
    }

    public Car getCarById(long carId) {
        return mapCarEntityToCarModel(carRepository.findById(carId).orElseThrow(() -> new NoCarWithSuchIdException(carId)));
    }

    public List<Car> getAllCarsByEmployeeId(long employeeId) {
        return carRepository.findAllCarsByEmployeeId(employeeId).stream()
                .map(this::mapCarEntityToCarModel)
                .collect(Collectors.toList());
    }

    public void saveCar(Car car) {
        carRepository.save(mapCarModelToCarEntity(car));
    }

    public void deleteCar(long carId) {
        carRepository.deleteById(carId);
    }

    private Car mapCarEntityToCarModel(CarEntity entity) {
        return Car.builder()
                .id(entity.getId())
                .manufacturer(entity.getManufacturer())
                .model(entity.getModel())
                .vinNumber(entity.getVinNumber())
                .color(entity.getColor().toModel())
                .build();
    }

    private CarEntity mapCarModelToCarEntity(Car car) {
        return CarEntity.builder()
                .id(car.getId())
                .manufacturer(car.getManufacturer())
                .model(car.getModel())
                .vinNumber(car.getVinNumber())
                .color(car.getColor().toEntity())
                .employeeId(car.getEmployeeId())
                .build();
    }
}
