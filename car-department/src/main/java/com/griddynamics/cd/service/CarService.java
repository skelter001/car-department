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

    private final CarRepository carRepository;
    private final ColorService colorService;

    public List<Car> getAllCars() {
        return StreamSupport.stream(carRepository.findAll().spliterator(), false)
                .map(this::mapCarEntityToCarModel)
                .collect(Collectors.toList());
    }

    public Car getCarById(Long carId) {
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

    public void deleteCar(Long carId) {
        carRepository.deleteById(carId);
    }

    public Car mapCarEntityToCarModel(CarEntity entity) {
        return Car.builder()
                .id(entity.getId())
                .manufacturer(entity.getManufacturer())
                .model(entity.getModel())
                .vinNumber(entity.getVinNumber())
                .color(colorService.mapColorEntityToColorModel(entity.getColor()))
                .employeeId(entity.getEmployeeId())
                .build();
    }

    public CarEntity mapCarModelToCarEntity(Car car) {
        return CarEntity.builder()
                .id(car.getId())
                .manufacturer(car.getManufacturer())
                .model(car.getModel())
                .vinNumber(car.getVinNumber())
                .employeeId(car.getEmployeeId())
                .color(colorService.mapColorModelToColorModel(car.getColor()))
                .build();
    }
}
