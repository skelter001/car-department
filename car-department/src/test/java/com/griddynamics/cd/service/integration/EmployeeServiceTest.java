package com.griddynamics.cd.service.integration;

import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.exception.EntityDeleteException;
import com.griddynamics.cd.mapper.EmployeeMapper;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.DepartmentType;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.model.create.CreateEmployeeRequest;
import com.griddynamics.cd.model.update.UpdateEmployeeRequest;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.DepartmentRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import com.griddynamics.cd.service.EmployeeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeServiceTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14"))
            .withDatabaseName("car_department_database")
            .withUsername("admin")
            .withPassword("password");

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @AfterAll
    static void tearDown() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        DepartmentEntity departmentEntity1 = DepartmentEntity.builder()
                .name("department 1")
                .email("test1@test")
                .description("some desc.")
                .departmentType(DepartmentType.SALE)
                .build();
        DepartmentEntity departmentEntity2 = DepartmentEntity.builder()
                .name("department 2")
                .email("test2@test")
                .description("some desc.")
                .departmentType(DepartmentType.SUPPORT)
                .build();

        departmentRepository.saveAll(List.of(departmentEntity1, departmentEntity2));

        EmployeeEntity employeeEntity1 = EmployeeEntity.builder()
                .firstName("Alfred")
                .lastName("Miles")
                .address("Atlanta, Georgia US.")
                .birthday(LocalDate.of(1995, 6, 21))
                .phoneNumber("4539832543")
                .department(departmentEntity1)
                .build();
        EmployeeEntity employeeEntity2 = EmployeeEntity.builder()
                .firstName("Darius")
                .lastName("Epps")
                .address("Abuja, Nigeria")
                .birthday(LocalDate.of(1993, 8, 2))
                .phoneNumber("5738310041")
                .department(departmentEntity2)
                .build();
        EmployeeEntity employeeEntity3 = EmployeeEntity.builder()
                .firstName("Earnest")
                .lastName("Marks")
                .address("Atlanta, Georgia US.")
                .birthday(LocalDate.of(1995, 12, 19))
                .phoneNumber("7630894488")
                .department(departmentEntity2)
                .build();
        EmployeeEntity employeeEntity4 = EmployeeEntity.builder()
                .firstName("Khris")
                .lastName("Tracy")
                .address("Augusta, Georgia US.")
                .birthday(LocalDate.of(1991, 4, 8))
                .phoneNumber("6649329842")
                .department(departmentEntity2)
                .build();

        employeeRepository.saveAll(List.of(employeeEntity1, employeeEntity2, employeeEntity3, employeeEntity4));
    }

    @AfterEach
    void tearDownEach() {
        carRepository.deleteAll();
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    private List<Employee> getAllEmployeesData() {
        return List.of(
                Employee.builder()
                        .id(1L)
                        .firstName("Alfred")
                        .lastName("Miles")
                        .address("Atlanta, Georgia US.")
                        .birthday(LocalDate.of(1995, 6, 21))
                        .phoneNumber("4539832543")
                        .departmentId(1L)
                        .build(),
                Employee.builder()
                        .id(2L)
                        .firstName("Darius")
                        .lastName("Epps")
                        .address("Abuja, Nigeria")
                        .birthday(LocalDate.of(1993, 8, 2))
                        .phoneNumber("5738310041")
                        .departmentId(2L)
                        .build(),
                Employee.builder()
                        .id(3L)
                        .firstName("Earnest")
                        .lastName("Marks")
                        .address("Atlanta, Georgia US.")
                        .birthday(LocalDate.of(1995, 12, 19))
                        .phoneNumber("7630894488")
                        .departmentId(2L)
                        .build(),
                Employee.builder()
                        .id(4L)
                        .firstName("Khris")
                        .lastName("Tracy")
                        .address("Augusta, Georgia US.")
                        .birthday(LocalDate.of(1991, 4, 8))
                        .phoneNumber("6649329842")
                        .departmentId(2L)
                        .build()
        );
    }

    @Test
    @Order(1)
    void getAllEmployees_whenSaveToEmployeeRepository_thenReturnValidList() {
        List<Employee> expected = getAllEmployeesData();

        assertEquals(expected, employeeService.getAllEmployees());
    }

    @Test
    @Order(2)
    void getDepartmentById_whenPassValidEmployeeIdTwoTimes_thenReturnValidModel() {
        Employee expected1 = Employee.builder()
                .id(6L)
                .firstName("Darius")
                .lastName("Epps")
                .address("Abuja, Nigeria")
                .birthday(LocalDate.of(1993, 8, 2))
                .phoneNumber("5738310041")
                .departmentId(4L)
                .build();
        Employee expected2 = Employee.builder()
                .id(8L)
                .firstName("Khris")
                .lastName("Tracy")
                .address("Augusta, Georgia US.")
                .birthday(LocalDate.of(1991, 4, 8))
                .phoneNumber("6649329842")
                .departmentId(4L)
                .build();

        assertEquals(expected1, employeeService.getEmployeeById(6L));
        assertEquals(expected2, employeeService.getEmployeeById(8L));
    }

    @Test
    void getEmployeeById_whenPassInvalidEmployeeId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.getEmployeeById(123L)
        );
        assertEquals("Employee with 123 id was not found", thrown.getMessage());
    }

    private List<Employee> getEmployeesByDepartmentIdData() {
        return List.of(
                Employee.builder()
                        .id(10L)
                        .firstName("Darius")
                        .lastName("Epps")
                        .address("Abuja, Nigeria")
                        .birthday(LocalDate.of(1993, 8, 2))
                        .phoneNumber("5738310041")
                        .departmentId(6L)
                        .build(),
                Employee.builder()
                        .id(11L)
                        .firstName("Earnest")
                        .lastName("Marks")
                        .address("Atlanta, Georgia US.")
                        .birthday(LocalDate.of(1995, 12, 19))
                        .phoneNumber("7630894488")
                        .departmentId(6L)
                        .build(),
                Employee.builder()
                        .id(12L)
                        .firstName("Khris")
                        .lastName("Tracy")
                        .address("Augusta, Georgia US.")
                        .birthday(LocalDate.of(1991, 4, 8))
                        .phoneNumber("6649329842")
                        .departmentId(6L)
                        .build()
        );
    }

    @Test
    @Order(3)
    void getEmployeesByDepartmentId_whenCallMethodTwoTimes_thenReturnValidListOfEmployeeModels() {
        List<Employee> expected1 = List.of(
                Employee.builder()
                        .id(9L)
                        .firstName("Alfred")
                        .lastName("Miles")
                        .address("Atlanta, Georgia US.")
                        .birthday(LocalDate.of(1995, 6, 21))
                        .phoneNumber("4539832543")
                        .departmentId(5L)
                        .build()
        );
        List<Employee> expected2 = getEmployeesByDepartmentIdData();

        assertEquals(expected1, employeeService.getEmployeesByDepartmentId(5L));
        assertEquals(expected2, employeeService.getEmployeesByDepartmentId(6L));
    }

    @Test
    @Order(4)
    void saveDepartment_whenPassValidCreateDepartmentRequest_thenReturnValidModel() {
        CreateEmployeeRequest createEmployeeRequest = CreateEmployeeRequest.builder()
                .firstName("Vanessa")
                .lastName("Keefer")
                .address("Atlanta, Georgia US.")
                .birthday(LocalDate.of(1998, 12, 15))
                .phoneNumber("8873431214")
                .departmentId(7L)
                .build();
        Employee expected = Employee.builder()
                .id(17L)
                .firstName("Vanessa")
                .lastName("Keefer")
                .address("Atlanta, Georgia US.")
                .birthday(LocalDate.of(1998, 12, 15))
                .phoneNumber("8873431214")
                .departmentId(7L)
                .build();

        assertEquals(expected, employeeService.saveEmployee(createEmployeeRequest));
    }

    @Test
    void saveEmployee_whenPassCreateEmployeeRequestWithExistingPhoneNumber_thenThrowEntityExistsException() {
        CreateEmployeeRequest createEmployeeRequest = CreateEmployeeRequest.builder()
                .firstName("Van")
                .lastName("Keefer")
                .phoneNumber("7630894488")
                .build();

        EntityExistsException thrown = assertThrows(
                EntityExistsException.class,
                () -> employeeService.saveEmployee(createEmployeeRequest)
        );

        assertEquals("Employee with 7630894488 phone number already exist", thrown.getMessage());
    }

    @Test
    void saveEmployee_whenPassCreateCarRequestWithInvalidDepartmentId_thenThrowEntityNotFoundException() {
        CreateEmployeeRequest createEmployeeRequest = CreateEmployeeRequest.builder()
                .firstName("Van")
                .lastName("Keefer")
                .departmentId(111L)
                .build();

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.saveEmployee(createEmployeeRequest)
        );

        assertEquals("Department with 111 id was not found", thrown.getMessage());
    }

    @Test
    @Order(5)
    void updateDepartment_whenPassValidUpdateDepartmentRequest_thenReturnValidModel() {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .firstName("Vanessa")
                .lastName("Keefer")
                .address("Atlanta, Georgia US.")
                .birthday(LocalDate.of(1998, 12, 15))
                .phoneNumber("8873431214")
                .departmentId(9L)
                .build();

        Employee expected = Employee.builder()
                .id(20L)
                .firstName("Vanessa")
                .lastName("Keefer")
                .address("Atlanta, Georgia US.")
                .birthday(LocalDate.of(1998, 12, 15))
                .phoneNumber("8873431214")
                .departmentId(9L)
                .build();

        assertEquals(expected, employeeService.updateEmployee(updateEmployeeRequest, 20L));
    }

    @Test
    void updateEmployee_whenPassInvalidEmployeeId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.updateEmployee(new UpdateEmployeeRequest(), 123L)
        );
        assertEquals("Employee with 123 id was not found", thrown.getMessage());
    }

    @Test
    @Order(6)
    void updateEmployee_whenPassUpdateEmployeeRequestWithExistingPhoneNumber_thenThrowEntityExistsException() {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .phoneNumber("7630894488")
                .build();

        EntityExistsException thrown = assertThrows(
                EntityExistsException.class,
                () -> employeeService.updateEmployee(updateEmployeeRequest, 25L)
        );
        assertEquals("Employee with 7630894488 phone number already exist", thrown.getMessage());
    }

    @Test
    @Order(7)
    void updateEmployee_whenPassUpdateEmployeeRequestWithInvalidDepartmentId_thenThrowEntityNotFoundException() {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .departmentId(118L)
                .build();

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.updateEmployee(updateEmployeeRequest, 26L)
        );

        assertEquals("Department with 118 id was not found", thrown.getMessage());
    }

    @Test
    @Order(8)
    void deleteDepartment_whenPassValidDepartmentId_thenCheckIfEntityActuallyDeleted() {
        employeeService.deleteEmployee(30L);
        assertFalse(employeeRepository.existsById(30L));
    }

    @Test
    void deleteEmployee_whenPassInvalidEmployeeId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.deleteEmployee(114L)
        );

        assertEquals("Employee with 114 id was not found", thrown.getMessage());
    }

    @Test
    @Order(9)
    void deleteEmployee_whenPassEmployeeIdWithDependentCars_thenThrowEntityDeleteException() {
        EmployeeEntity employeeEntity = employeeRepository.getById(34L);

        CarEntity carEntity = CarEntity.builder()
                .manufacturer("Audi")
                .model("A2")
                .vinNumber("JH4KA8271NC000480")
                .employee(employeeEntity)
                .color(Color.WHITE)
                .build();
        carRepository.save(carEntity);

        EntityDeleteException thrown = assertThrows(
                EntityDeleteException.class,
                () -> employeeService.deleteEmployee(34L)
        );

        assertEquals("Unable to delete employee with id 34", thrown.getMessage());
    }
}