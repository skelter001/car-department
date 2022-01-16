package com.griddynamics.cd.controller;

import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/cars")
    @Operation(
            summary = "Get all cars",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/cars/{carId}")
    @Operation(
            summary = "Get car by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public Car getCarById(@PathVariable Long carId) {
        return carService.getCarById(carId);
    }

    @GetMapping("/employees/{employeeId}/cars")
    @Operation(
            summary = "Get all cars from the specific employee",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public List<Car> getCarsByEmployeeId(@PathVariable Long employeeId) {
        return carService.getCarsByEmployeeId(employeeId);
    }

    @PostMapping("/cars")
    @Operation(
            summary = "Save car model",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public Car saveCar(@RequestBody @Valid CreateCarRequest createCarRequest) {
        return carService.saveCar(createCarRequest);
    }

    @PutMapping("/cars/{carId}")
    @Operation(
            summary = "Update car model",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public Car updateCar(@PathVariable Long carId,
                         @RequestBody @Valid UpdateCarRequest updateCarRequest) {
        return carService.updateCar(updateCarRequest, carId);
    }

    @DeleteMapping("/cars/{carId}")
    @Operation(
            summary = "Delete car by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public void deleteCarById(@PathVariable Long carId) {
        carService.deleteCar(carId);
    }
}
