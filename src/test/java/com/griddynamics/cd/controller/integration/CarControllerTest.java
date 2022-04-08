package com.griddynamics.cd.controller.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.cd.BaseIntegrationTest;
import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class CarControllerTest extends BaseIntegrationTest {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private final Connection connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
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

    public CarControllerTest() throws SQLException {
    }

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
    void getAllCars_whenSaveToCarRepository_thenReturnValidList() throws Exception {
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(cars)));
    }

    @Test
    void getCarById_whenPassValidIdTwoTimes_thenReturnValidModel() throws Exception {
        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(cars.get(0))));
        mockMvc.perform(get("/cars/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(cars.get(2))));
    }

    @Test
    void getCarById_whenPassInvalidCarId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(get("/cars/123"))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Car with 123 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void getCarsByEmployeeId_whenCallMethodTwoTimes_thenReturnValidListOfEmployeeModels() throws Exception {
        mockMvc.perform(get("/employees/1/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(cars.get(0)))));
        mockMvc.perform(get("/employees/2/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(cars.get(1), cars.get(2), cars.get(3)))));
    }

    @Test
    void saveCar_whenPassValidCreateCarRequest_thenReturnValidModel() throws Exception {
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

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void saveCar_whenPassCreateCarRequestWithInvalidEmployeeId_thenThrowEntityNotFoundException() throws Exception {
        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .color(Color.WHITE)
                .employeeId(123L)
                .build();

        MvcResult result = mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Employee with 123 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void updateCar_whenPassValidUpdateCarRequest_thenReturnValidModel() throws Exception {
        UpdateCarRequest updateCarRequest = UpdateCarRequest.builder()
                .manufacturer("Audi")
                .model("A2")
                .vinNumber("JH4KA8271NC000480")
                .employeeId(2L)
                .color(Color.WHITE)
                .build();

        Car expected = Car.builder()
                .id(1L)
                .manufacturer("Audi")
                .model("A2")
                .vinNumber("JH4KA8271NC000480")
                .employeeId(2L)
                .color(Color.WHITE)
                .build();

        mockMvc.perform(put("/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCarRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void updateCar_whenPassInvalidCarId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(put("/cars/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateCarRequest())))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Car with 123 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void updateCar_whenPassUpdateCarRequestWithInvalidEmployeeId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(put("/cars/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UpdateCarRequest.builder()
                                .employeeId(54L)
                                .build())))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Employee with 54 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void deleteCarById_whenPassValidCarId_thenCheckIfEntityActuallyDeleted() throws Exception {
        mockMvc.perform(delete("/cars/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        assertFalse(carRepository.existsById(3L));
    }

    @Test
    void deleteCarById_whenPassInvalidCarId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(delete("/cars/12"))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Car with 12 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }
}
