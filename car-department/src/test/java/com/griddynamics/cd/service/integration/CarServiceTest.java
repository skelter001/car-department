package com.griddynamics.cd.service.integration;

import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.mapper.CarMapper;
import com.griddynamics.cd.model.Car;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.create.CreateCarRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import com.griddynamics.cd.service.CarService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarServiceTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer(DockerImageName.parse("postgres:14"))
            .withDatabaseName("car_department_database")
            .withUsername("admin")
            .withPassword("password");

    private final CarRepository carRepository;
    private final EmployeeRepository employeeRepository;
    private final CarService carService;

    @Autowired
    public CarServiceTest(CarRepository carRepository, EmployeeRepository employeeRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.employeeRepository = employeeRepository;
        carService = new CarService(carRepository, employeeRepository, carMapper);
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @AfterAll
    static void tearDown() {
        container.close();
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
    void getAllCars_whenSaveToCarRepository_thenReturnValidList() {
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

        assertEquals(expected, carService.getAllCars());
    }

    @Test
    @Order(2)
    void getCarById_whenPassValidIdTwoTimes_thenReturnValidModel() {
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

        assertEquals(expected1, carService.getCarById(6L));
        assertEquals(expected2, carService.getCarById(8L));
    }

    @Test
    void getCarById_whenPassInvalidCarId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.getCarById(123L)
        );
        assertEquals("Car with 123 id was not found", thrown.getMessage());
    }

    @Test
    @Order(3)
    void getCarsByEmployeeId_whenCallMethodTwoTimes_thenReturnValidListOfEmployeeModels() {
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

        assertEquals(expected1, carService.getCarsByEmployeeId(5L));
        assertEquals(expected2, carService.getCarsByEmployeeId(6L));
    }

    @Test
    @Order(4)
    void saveCar_whenPassValidCreateCarRequest_thenReturnValidModel() {
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

        assertEquals(expected, carService.saveCar(createCarRequest));
    }

    @Test
    void saveCar_whenPassCreateCarRequestWithInvalidEmployeeId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.saveCar(CreateCarRequest.builder()
                        .employeeId(123L)
                        .build())
        );
        assertEquals("Employee with 123 id was not found", thrown.getMessage());
    }

    @Test
    @Order(5)
    void updateCar_whenPassValidUpdateCarRequest_thenReturnValidModel() {
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

        assertEquals(expected, carService.updateCar(updateCarRequest, 19L));
    }

    @Test
    void updateCar_whenPassInvalidCarId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.updateCar(new UpdateCarRequest(), 123L)
        );
        assertEquals("Car with 123 id was not found", thrown.getMessage());
    }

    @Test
    @Order(7)
    void updateCar_whenPassUpdateCarRequestWithInvalidEmployeeId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.updateCar(UpdateCarRequest.builder()
                        .employeeId(54L)
                        .build(), 27L)
        );
        assertEquals("Employee with 54 id was not found", thrown.getMessage());
    }

    @Test
    @Order(6)
    void deleteCar_whenPassValidCarId_thenCheckIfEntityActuallyDeleted() {
        carService.deleteCar(22L);
        assertFalse(carRepository.existsById(22L));
    }

    @Test
    void deleteCar_whenPassInvalidCarId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> carService.deleteCar(12L)
        );
        assertEquals("Car with 12 id was not found", thrown.getMessage());
    }
}
