package com.griddynamics.cd.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.griddynamics.cd.controller.EmployeeController;
import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.exception.ExceptionAdviser;
import com.griddynamics.cd.mapper.EmployeeMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer(DockerImageName.parse("postgres:14"))
            .withDatabaseName("car_department_database")
            .withUsername("admin")
            .withPassword("password");

    private final MockMvc mockMvc;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final CarRepository carRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmployeeControllerTest(EmployeeRepository employeeRepository,
                                  DepartmentRepository departmentRepository,
                                  CarRepository carRepository,
                                  EmployeeMapper employeeMapper,
                                  ExceptionAdviser exceptionAdviser) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.carRepository = carRepository;
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new EmployeeController(
                        new EmployeeService(
                                employeeRepository,
                                departmentRepository,
                                carRepository,
                                employeeMapper)
                ))
                .setControllerAdvice(exceptionAdviser)
                .build();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
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

    @Test
    @Order(1)
    void getAllEmployees_whenSaveToEmployeeRepository_thenReturnValidList() throws Exception {
        List<Employee> expected = List.of(
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

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @Order(2)
    void getDepartmentById_whenPassValidEmployeeIdTwoTimes_thenReturnValidModel() throws Exception {
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

        mockMvc.perform(get("/employees/6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected1)));

        mockMvc.perform(get("/employees/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected2)));
    }

    @Test
    @Order(3)
    void getEmployeesByDepartmentId_whenCallMethodTwoTimes_thenReturnValidListOfEmployeeModels() throws Exception {
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
        List<Employee> expected2 = List.of(
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


        mockMvc.perform(get("/departments/5/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected1)));

        mockMvc.perform(get("/departments/6/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected2)));
    }

    @Test
    @Order(4)
    void saveDepartment_whenPassValidCreateDepartmentRequest_thenReturnValidModel() throws Exception {
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

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @Order(5)
    void updateDepartment_whenPassValidUpdateDepartmentRequest_thenReturnValidModel() throws Exception {
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

        mockMvc.perform(put("/employees/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @Order(6)
    void deleteDepartment_whenPassValidDepartmentId_thenCheckIfEntityActuallyDeleted() throws Exception {
        mockMvc.perform(delete("/employees/24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        assertFalse(employeeRepository.existsById(24L));
    }
}
