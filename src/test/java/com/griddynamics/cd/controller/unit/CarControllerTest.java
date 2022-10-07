package com.griddynamics.cd.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.cd.controller.CarController;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CarControllerTest {

    private final CarService carService = mock(CarService.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new CarController(carService)).build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllCars_whenCallMethod_thenReturnOk() throws Exception {
        when(carService.getCarsWithFiltering(
                anyList(),
                anyList(),
                anyList(),
                anyList(),
                anyList(),
                anyInt(),
                anyInt(),
                anyString(),
                any(Sort.Direction.class)
        ))
                .thenReturn(ResponseEntity.of(Optional.empty()));

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keyToNull").doesNotExist());
    }

    @Test
    void getCarById_whenPassValidId_thenReturnOk() throws Exception {
        when(carService.getCarById(1L))
                .thenReturn(new Car());

        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void getCarsByEmployeeId_whenPassValidEmployeeId_thenReturnOk() throws Exception {
        when(carService.getCarsByEmployeeId(3L))
                .thenReturn(List.of(new Car(), new Car()));

        mockMvc.perform(get("/employees/3/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void saveCar_whenValidCreateCarRequest_thenReturnOk() throws Exception {
        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .manufacturer("Audi")
                .model("A5")
                .vinNumber("JH4KA7530MC011312")
                .color(Color.WHITE)
                .employeeId(2L)
                .build();

        when(carService.saveCar(any(CreateCarRequest.class)))
                .thenReturn(new Car());

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void saveCar_whenCreateCarRequestWithoutColor_thenReturnBadRequest() throws Exception {
        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .employeeId(3L)
                .build();

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveCar_whenCreateCarRequestWithInvalidEmployeeId_thenReturnBadRequest() throws Exception {
        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .color(Color.WHITE)
                .employeeId(-123L)
                .build();

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveCar_whenCreateCarRequestWithInvalidVinNumber_thenReturnBadRequest() throws Exception {
        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .color(Color.GREY)
                .vinNumber("asd")
                .employeeId(3L)
                .build();

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCar_whenValidUpdateCarRequest_thenReturnOk() throws Exception {
        UpdateCarRequest updateCarRequest = UpdateCarRequest.builder()
                .manufacturer("Audi")
                .model("A7")
                .vinNumber("KNDJE723297570351")
                .color(Color.BLACK)
                .employeeId(1L)
                .build();

        when(carService.updateCar(any(UpdateCarRequest.class), anyLong()))
                .thenReturn(new Car());

        mockMvc.perform(put("/cars/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCarRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void updateCar_whenUpdateCarRequestWithInvalidManufacturer_thenReturnOk() throws Exception {
        UpdateCarRequest updateCarRequest = UpdateCarRequest
                .builder()
                .manufacturer("@$%")
                .employeeId(1L)
                .build();

        mockMvc.perform(put("/cars/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCarRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCarById_whenPassValidId_thenReturnOk() throws Exception {
        doNothing().when(carService)
                .deleteCar(anyLong());

        mockMvc.perform(delete("/cars/21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
