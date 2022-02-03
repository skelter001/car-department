package com.griddynamics.cd.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.cd.controller.DepartmentController;
import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.exception.ExceptionAdviser;
import com.griddynamics.cd.mapper.DepartmentMapper;
import com.griddynamics.cd.model.Department;
import com.griddynamics.cd.model.DepartmentType;
import com.griddynamics.cd.model.create.CreateDepartmentRequest;
import com.griddynamics.cd.model.update.UpdateDepartmentRequest;
import com.griddynamics.cd.repository.DepartmentRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import com.griddynamics.cd.service.DepartmentService;
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

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartmentControllerTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer(DockerImageName.parse("postgres:14"))
            .withDatabaseName("car_department_database")
            .withUsername("admin")
            .withPassword("password");

    private final MockMvc mockMvc;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public DepartmentControllerTest(DepartmentRepository departmentRepository,
                                    EmployeeRepository employeeRepository,
                                    DepartmentMapper departmentMapper,
                                    ExceptionAdviser exceptionAdviser) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new DepartmentController(
                        new DepartmentService(departmentRepository,
                                employeeRepository,
                                departmentMapper)
                ))
                .setControllerAdvice(exceptionAdviser)
                .build();
        this.objectMapper = new ObjectMapper();
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
        DepartmentEntity departmentEntity3 = DepartmentEntity.builder()
                .name("department 3")
                .email("test3@test")
                .description("some desc.")
                .departmentType(DepartmentType.PROVIDER)
                .build();
        DepartmentEntity departmentEntity4 = DepartmentEntity.builder()
                .name("department 4")
                .email("test4@test")
                .description("some desc.")
                .departmentType(DepartmentType.SUPPORT)
                .build();

        departmentRepository.saveAll(List.of(departmentEntity1, departmentEntity2, departmentEntity3, departmentEntity4));
    }

    @AfterEach
    void tearDownEach() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    @Order(1)
    void getAllDepartments_whenSaveToDepartmentRepository_thenReturnValidList() throws Exception {
        List<Department> expected = List.of(
                Department.builder()
                        .id(1L)
                        .name("department 1")
                        .email("test1@test")
                        .description("some desc.")
                        .departmentType(DepartmentType.SALE)
                        .build(),
                Department.builder()
                        .id(2L)
                        .name("department 2")
                        .email("test2@test")
                        .description("some desc.")
                        .departmentType(DepartmentType.SUPPORT)
                        .build(),
                Department.builder()
                        .id(3L)
                        .name("department 3")
                        .email("test3@test")
                        .description("some desc.")
                        .departmentType(DepartmentType.PROVIDER)
                        .build(),
                Department.builder()
                        .id(4L)
                        .name("department 4")
                        .email("test4@test")
                        .description("some desc.")
                        .departmentType(DepartmentType.SUPPORT)
                        .build()
        );


        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @Order(2)
    void getDepartmentById_whenPassValidIdTwoTimes_thenReturnValidModel() throws Exception {
        Department expected1 = Department.builder()
                .id(6L)
                .name("department 2")
                .email("test2@test")
                .description("some desc.")
                .departmentType(DepartmentType.SUPPORT)
                .build();
        Department expected2 = Department.builder()
                .id(8L)
                .name("department 4")
                .email("test4@test")
                .description("some desc.")
                .departmentType(DepartmentType.SUPPORT)
                .build();

        mockMvc.perform(get("/departments/6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected1)));

        mockMvc.perform(get("/departments/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected2)));
    }

    @Test
    @Order(3)
    void saveDepartment_whenPassValidCreateDepartmentRequest_thenReturnValidModel() throws Exception {
        CreateDepartmentRequest createDepartmentRequest = CreateDepartmentRequest.builder()
                .name("department")
                .email("dep@dep")
                .description("smth")
                .departmentType(DepartmentType.PROVIDER)
                .build();

        Department expected = Department.builder()
                .id(13L)
                .name("department")
                .email("dep@dep")
                .description("smth")
                .departmentType(DepartmentType.PROVIDER)
                .build();


        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDepartmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @Order(4)
    void updateDepartment_whenPassValidUpdateDepartmentRequest_thenReturnValidModel() throws Exception {
        UpdateDepartmentRequest updateDepartmentRequest = UpdateDepartmentRequest.builder()
                .name("new name")
                .email("new@mail")
                .description("new desc")
                .departmentType(DepartmentType.SUPPORT)
                .build();

        Department expected = Department.builder()
                .id(14L)
                .name("new name")
                .email("new@mail")
                .description("new desc")
                .departmentType(DepartmentType.SUPPORT)
                .build();

        mockMvc.perform(put("/departments/14")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDepartmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @Order(5)
    void deleteDepartment_whenPassValidDepartmentId_thenCheckIfEntityActuallyDeleted() throws Exception {
        mockMvc.perform(delete("/departments/19"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        assertFalse(departmentRepository.existsById(19L));
    }
}