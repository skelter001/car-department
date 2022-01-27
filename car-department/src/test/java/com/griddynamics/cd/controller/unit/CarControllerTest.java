package com.griddynamics.cd.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.cd.controller.CarController;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {CarController.class, ObjectMapper.class})
@AutoConfigureMockMvc
@EnableWebMvc
class CarControllerTest {

    @MockBean
    private CarService carService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCars_whenPerformMethod_thenReturnOk() throws Exception {
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
    void getCarById() throws Exception {
        mockMvc.perform(get("/cars/12"))
                .andExpect(status().isNotFound())
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

        when(carService.saveCar(any(CreateCarRequest.class)))
                .thenReturn(mock(Car.class));

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

        when(carService.saveCar(any(CreateCarRequest.class)))
                .thenReturn(mock(Car.class));

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

        when(carService.saveCar(any(CreateCarRequest.class)))
                .thenReturn(mock(Car.class));

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCar_whenValidUpdateCarRequest_thenReturnOk() throws Exception {
        UpdateCarRequest updateCarRequest = mock(UpdateCarRequest.class);

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

        when(carService.updateCar(any(UpdateCarRequest.class), anyLong()))
                .thenReturn(mock(Car.class));

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