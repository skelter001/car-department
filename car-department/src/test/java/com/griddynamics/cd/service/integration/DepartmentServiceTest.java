package com.griddynamics.cd.service.integration;

import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.exception.EntityDeleteException;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartmentServiceTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer(DockerImageName.parse("postgres:14"))
            .withDatabaseName("car_department_database")
            .withUsername("admin")
            .withPassword("password");

    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentServiceTest(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.departmentService = new DepartmentService(departmentRepository, employeeRepository, departmentMapper);
    }

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
    void getAllDepartments_whenSaveToDepartmentRepository_thenReturnValidList() {
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

        assertEquals(expected, departmentService.getAllDepartments());
    }

    @Test
    @Order(2)
    void getDepartmentById_whenPassValidIdTwoTimes_thenReturnValidModel() {
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

        assertEquals(expected1, departmentService.getDepartmentById(6L));
        assertEquals(expected2, departmentService.getDepartmentById(8L));
    }

    @Test
    void getDepartmentById_whenPassInvalidDepartmentId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> departmentService.getDepartmentById(100L)
        );
        assertEquals("Department with 100 id was not found", thrown.getMessage());
    }

    @Test
    @Order(3)
    void saveDepartment_whenPassValidCreateDepartmentRequest_thenReturnValidModel() {
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

        assertEquals(expected, departmentService.saveDepartment(createDepartmentRequest));
    }

    @Test
    void saveDepartment_whenPassCreateDepartmentRequestWithExistingEmail_thenThrowEntityExistsException() {
        CreateDepartmentRequest createDepartmentRequest = CreateDepartmentRequest.builder()
                .name("department")
                .email("test1@test")
                .description("smth")
                .departmentType(DepartmentType.PROVIDER)
                .build();

        EntityExistsException thrown = assertThrows(
                EntityExistsException.class,
                () -> departmentService.saveDepartment(createDepartmentRequest)
        );

        assertEquals("Department with test1@test email already exist", thrown.getMessage());
    }

    @Test
    @Order(4)
    void updateDepartment_whenPassValidUpdateDepartmentRequest_thenReturnValidModel() {
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

        assertEquals(expected, departmentService.updateDepartment(updateDepartmentRequest, 14L));
    }

    @Test
    void updateDepartment_whenPassWrongDepartmentId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> departmentService.updateDepartment(new UpdateDepartmentRequest(), 123L));

        assertEquals("Department with 123 id was not found", thrown.getMessage());
    }

    @Test
    @Order(6)
    void updateDepartment_whenPassUpdateDepartmentRequestWithExistingEmail_thenThrowEntityExistsException() {
        UpdateDepartmentRequest updateDepartmentRequest = UpdateDepartmentRequest.builder()
                .name("new name")
                .email("test1@test")
                .description("new desc")
                .departmentType(DepartmentType.SUPPORT)
                .build();

        EntityExistsException thrown = assertThrows(
                EntityExistsException.class,
                () -> departmentService.updateDepartment(updateDepartmentRequest, 23L)
        );

        assertEquals("Department with test1@test email already exist", thrown.getMessage());
    }

    @Test
    @Order(5)
    void deleteDepartment_whenPassValidDepartmentId_thenCheckIfEntityActuallyDeleted() {
        departmentService.deleteDepartment(19L);
        assertFalse(departmentRepository.existsById(19L));
    }

    @Test
    void deleteDepartment_whenPassInvalidDepartmentId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> departmentService.deleteDepartment(114L)
        );

        assertEquals("Department with 114 id was not found", thrown.getMessage());
    }

    @Test
    @Order(7)
    void deleteDepartment_whenPasDepartmentIdWithDependentEmployees_thenThrowEntityDeleteException() {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .firstName("Joe")
                .lastName("Doe")
                .department(departmentRepository.getById(26L))
                .build();
        employeeRepository.save(employeeEntity);

        EntityDeleteException thrown = assertThrows(
                EntityDeleteException.class,
                () -> departmentService.deleteDepartment(26L)
        );

        assertEquals("Unable to delete department with id 26", thrown.getMessage());
    }
}