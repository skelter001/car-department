package com.griddynamics.cd.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.cd.controller.CarController;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

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
        when(carService.getAllCars())
                .thenReturn(List.of(mock(Car.class)));

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void getCarById_whenPassValidId_thenReturnOk() throws Exception {
        when(carService.getCarById(1L))
                .thenReturn(mock(Car.class));

        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void getCarsByEmployeeId_whenPassValidEmployeeId_thenReturnOk() throws Exception {
        when(carService.getCarsByEmployeeId(3L))
                .thenReturn(List.of(mock(Car.class), mock(Car.class)));

        mockMvc.perform(get("/employees/3/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void saveCar_whenValidCreateCarRequest_thenReturnOk() throws Exception {
        CreateCarRequest createCarRequest = mock(CreateCarRequest.class);

        when(createCarRequest.getManufacturer())
                .thenReturn("Audi");
        when(createCarRequest.getModel())
                .thenReturn("A5");
        when(createCarRequest.getVinNumber())
                .thenReturn("JH4KA7530MC011312");
        when(createCarRequest.getColor())
                .thenReturn(mock(Color.class));
        when(createCarRequest.getEmployeeId())
                .thenReturn(2L);

        when(carService.saveCar(any(CreateCarRequest.class)))
                .thenReturn(mock(Car.class));

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void saveCar_whenCreateCarRequestWithNullColor_thenReturnBadRequest() throws Exception {
        CreateCarRequest createCarRequest = mock(CreateCarRequest.class);

        when(createCarRequest.getEmployeeId())
                .thenReturn(3L);

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveCar_whenCreateCarRequestWithInvalidEmployeeId_thenReturnBadRequest() throws Exception {
        CreateCarRequest createCarRequest = mock(CreateCarRequest.class);

        when(createCarRequest.getColor())
                .thenReturn(mock(Color.class));
        when(createCarRequest.getEmployeeId())
                .thenReturn(-123L);

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveCar_whenCreateCarRequestWithInvalidVinNumber_thenReturnBadRequest() throws Exception {
        CreateCarRequest createCarRequest = mock(CreateCarRequest.class);

        when(createCarRequest.getColor())
                .thenReturn(mock(Color.class));
        when(createCarRequest.getEmployeeId())
                .thenReturn(3L);
        when(createCarRequest.getVinNumber())
                .thenReturn("qwe");

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCar_whenValidUpdateCarRequest_thenReturnOk() throws Exception {
        UpdateCarRequest updateCarRequest = mock(UpdateCarRequest.class);

        when(updateCarRequest.getManufacturer())
                .thenReturn("Audi");
        when(updateCarRequest.getModel())
                .thenReturn("A7");
        when(updateCarRequest.getVinNumber())
                .thenReturn("KNDJE723297570351");
        when(updateCarRequest.getColor())
                .thenReturn(mock(Color.class));
        when(updateCarRequest.getEmployeeId())
                .thenReturn(1L);

        when(carService.updateCar(any(UpdateCarRequest.class), anyLong()))
                .thenReturn(mock(Car.class));

        mockMvc.perform(put("/cars/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCarRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void updateCar_whenUpdateCarRequestWithInvalidManufacturer_thenReturnOk() throws Exception {
        UpdateCarRequest updateCarRequest = mock(UpdateCarRequest.class);

        when(updateCarRequest.getEmployeeId())
                .thenReturn(1L);
        when(updateCarRequest.getManufacturer())
                .thenReturn("@$%");

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