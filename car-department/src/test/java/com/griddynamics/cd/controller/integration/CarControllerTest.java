package com.griddynamics.cd.controller.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EnableWebMvc
@AutoConfigureMockMvc
public class CarControllerTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14"))
            .withDatabaseName("car_department_database")
            .withUsername("admin")
            .withPassword("password");

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @AfterAll
    static void tearDown() {
        container.stop();
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
    void tearDownEach() {
        carRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    @Order(1)
    void getAllCars_whenSaveToCarRepository_thenReturnValidList() throws Exception {
        List<Car> expected = List.of(
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

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @Order(2)
    void getCarById_whenPassValidIdTwoTimes_thenReturnValidModel() throws Exception {
        Car expected1 = Car.builder()
                .id(6L)
                .manufacturer("Nissan")
                .model("Silvia S13")
                .color(Color.GREY)
                .employeeId(4L)
                .build();
        Car expected2 = Car.builder()
                .id(8L)
                .manufacturer("Toyota")
                .model("Mark 2")
                .vinNumber("4T3ZK3BB7BU042861")
                .color(Color.BLACK)
                .employeeId(4L)
                .build();

        mockMvc.perform(get("/cars/6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected1)));
        mockMvc.perform(get("/cars/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected2)));
    }

    @Test
    void getCarById_whenPassInvalidCarId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(get("/cars/123"))
                .andExpect(status().isNotFound())
                .andReturn();
        Optional<EntityNotFoundException> thrown = Optional.ofNullable((EntityNotFoundException) result.getResolvedException());
        thrown.ifPresent(ex -> assertEquals("Car with 123 id was not found", ex.getMessage()));
    }

    @Test
    @Order(3)
    void getCarsByEmployeeId_whenCallMethodTwoTimes_thenReturnValidListOfEmployeeModels() throws Exception {
        List<Car> expected1 = List.of(
                Car.builder()
                        .id(9L)
                        .manufacturer("Honda")
                        .model("Coupe")
                        .vinNumber("4T3ZK3BB7BU042861")
                        .color(Color.BLACK)
                        .employeeId(5L)
                        .build()
        );
        List<Car> expected2 = List.of(
                Car.builder()
                        .id(10L)
                        .manufacturer("Nissan")
                        .model("Silvia S13")
                        .color(Color.GREY)
                        .employeeId(6L)
                        .build(),
                Car.builder()
                        .id(11L)
                        .manufacturer("Toyota")
                        .model("Chaser")
                        .vinNumber("1HGCG2254WA015540")
                        .color(Color.WHITE)
                        .employeeId(6L)
                        .build(),
                Car.builder()
                        .id(12L)
                        .manufacturer("Toyota")
                        .model("Mark 2")
                        .vinNumber("4T3ZK3BB7BU042861")
                        .color(Color.BLACK)
                        .employeeId(6L)
                        .build()
        );

        mockMvc.perform(get("/employees/5/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected1)));
        mockMvc.perform(get("/employees/6/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected2)));
    }

    @Test
    @Order(4)
    void saveCar_whenPassValidCreateCarRequest_thenReturnValidModel() throws Exception {
        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .manufacturer("Nissan")
                .model("Silvia S13")
                .employeeId(7L)
                .color(Color.GREY)
                .build();
        Car expected = Car.builder()
                .id(17L)
                .manufacturer("Nissan")
                .model("Silvia S13")
                .employeeId(7L)
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
        Optional<EntityNotFoundException> thrown = Optional.ofNullable((EntityNotFoundException) result.getResolvedException());
        thrown.ifPresent(ex -> assertEquals("Employee with 123 id was not found", ex.getMessage()));
    }

    @Test
    @Order(5)
    void updateCar_whenPassValidUpdateCarRequest_thenReturnValidModel() throws Exception {
        UpdateCarRequest updateCarRequest = UpdateCarRequest.builder()
                .manufacturer("Audi")
                .model("A2")
                .vinNumber("JH4KA8271NC000480")
                .employeeId(9L)
                .color(Color.WHITE)
                .build();

        Car expected = Car.builder()
                .id(19L)
                .manufacturer("Audi")
                .model("A2")
                .vinNumber("JH4KA8271NC000480")
                .employeeId(9L)
                .color(Color.WHITE)
                .build();

        mockMvc.perform(put("/cars/19")
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
        Optional<EntityNotFoundException> thrown = Optional.ofNullable((EntityNotFoundException) result.getResolvedException());
        thrown.ifPresent(ex -> assertEquals("Car with 123 id was not found", ex.getMessage()));
    }

    @Test
    @Order(6)
    void updateCar_whenPassUpdateCarRequestWithInvalidEmployeeId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(put("/cars/22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UpdateCarRequest.builder()
                                .employeeId(54L)
                                .build())))
                .andExpect(status().isNotFound())
                .andReturn();
        Optional<EntityNotFoundException> thrown = Optional.ofNullable((EntityNotFoundException) result.getResolvedException());
        thrown.ifPresent(ex -> assertEquals("Employee with 54 id was not found", ex.getMessage()));
    }

    @Test
    @Order(7)
    void deleteCarById_whenPassValidCarId_thenCheckIfEntityActuallyDeleted() throws Exception {
        mockMvc.perform(delete("/cars/27"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        assertFalse(carRepository.existsById(27L));
    }

    @Test
    void deleteCarById_whenPassInvalidCarId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(delete("/cars/12"))
                .andExpect(status().isNotFound())
                .andReturn();
        Optional<EntityNotFoundException> thrown = Optional.ofNullable((EntityNotFoundException) result.getResolvedException());
        thrown.ifPresent(ex -> assertEquals("Car with 12 id was not found", ex.getMessage()));
    }
}