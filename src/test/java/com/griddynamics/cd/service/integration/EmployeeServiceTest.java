package com.griddynamics.cd.service.integration;

import com.griddynamics.cd.BaseIntegrationTest;
import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.exception.EntityDeleteException;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.DepartmentType;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.model.create.CreateEmployeeRequest;
import com.griddynamics.cd.model.update.UpdateEmployeeRequest;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.DepartmentRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import com.griddynamics.cd.service.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class EmployeeServiceTest extends BaseIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;
    private final List<Employee> employees = List.of(
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
    void cleanUp() throws SQLException {
        Statement st = connection.createStatement();

        st.execute("TRUNCATE TABLE car, employee, department RESTART IDENTITY CASCADE ;");
        st.close();
    }

    @Test
    void getAllEmployees_whenSaveToEmployeeRepository_thenReturnValidList() {
        Map<String, Object> values = new HashMap<>();
        values.put("pageNumber", 0);
        values.put("pageSize", 3);
        values.put("totalPages", 1);
        values.put("totalObjects", 3L);
        values.put("employees", List.of(employees.get(0), employees.get(1), employees.get(2)));

        ResponseEntity<Map<String, Object>> expected = ResponseEntity.ok(values);
        ResponseEntity<Map<String, Object>> actual = employeeService.getEmployeesWithFiltering(
                null, null, null, null, null, null,
                0, 3, "first_name", Sort.Direction.ASC);

        assertEquals(expected, actual);
    }

    @Test
    void getDepartmentById_whenPassValidEmployeeIdTwoTimes_thenReturnValidModel() {
        assertEquals(employees.get(1), employeeService.getEmployeeById(2L));
        assertEquals(employees.get(3), employeeService.getEmployeeById(4L));
    }

    @Test
    void getEmployeeById_whenPassInvalidEmployeeId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.getEmployeeById(123L)
        );
        assertEquals("Employee with 123 id was not found", thrown.getMessage());
    }

    @Test
    void getEmployeesByDepartmentId_whenCallMethodTwoTimes_thenReturnValidListOfEmployeeModels() {
        assertEquals(List.of(employees.get(0)), employeeService.getEmployeesByDepartmentId(1L));
        assertEquals(List.of(employees.get(1), employees.get(2), employees.get(3)), employeeService.getEmployeesByDepartmentId(2L));
    }

    @Test
    void saveDepartment_whenPassValidCreateDepartmentRequest_thenReturnValidModel() {
        CreateEmployeeRequest createEmployeeRequest = CreateEmployeeRequest.builder()
                .firstName("Vanessa")
                .lastName("Keefer")
                .address("Atlanta, Georgia US.")
                .birthday(LocalDate.of(1998, 12, 15))
                .phoneNumber("8873431214")
                .departmentId(1L)
                .build();
        Employee expected = Employee.builder()
                .id(5L)
                .firstName("Vanessa")
                .lastName("Keefer")
                .address("Atlanta, Georgia US.")
                .birthday(LocalDate.of(1998, 12, 15))
                .phoneNumber("8873431214")
                .departmentId(1L)
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
    void updateDepartment_whenPassValidUpdateDepartmentRequest_thenReturnValidModel() {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .firstName("Vanessa")
                .lastName("Keefer")
                .address("Atlanta, Georgia US.")
                .birthday(LocalDate.of(1998, 12, 15))
                .phoneNumber("8873431214")
                .departmentId(2L)
                .build();

        Employee expected = Employee.builder()
                .id(3L)
                .firstName("Vanessa")
                .lastName("Keefer")
                .address("Atlanta, Georgia US.")
                .birthday(LocalDate.of(1998, 12, 15))
                .phoneNumber("8873431214")
                .departmentId(2L)
                .build();

        assertEquals(expected, employeeService.updateEmployee(updateEmployeeRequest, 3L));
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
    void updateEmployee_whenPassUpdateEmployeeRequestWithExistingPhoneNumber_thenThrowEntityExistsException() {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .phoneNumber("7630894488")
                .build();

        EntityExistsException thrown = assertThrows(
                EntityExistsException.class,
                () -> employeeService.updateEmployee(updateEmployeeRequest, 2L)
        );
        assertEquals("Employee with 7630894488 phone number already exist", thrown.getMessage());
    }

    @Test
    void updateEmployee_whenPassUpdateEmployeeRequestWithInvalidDepartmentId_thenThrowEntityNotFoundException() {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .departmentId(118L)
                .build();

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.updateEmployee(updateEmployeeRequest, 3L)
        );

        assertEquals("Department with 118 id was not found", thrown.getMessage());
    }

    @Test
    void deleteDepartment_whenPassValidDepartmentId_thenCheckIfEntityActuallyDeleted() {
        employeeService.deleteEmployee(3L);
        assertFalse(employeeRepository.existsById(3L));
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
    void deleteEmployee_whenPassEmployeeIdWithDependentCars_thenThrowEntityDeleteException() {
        EmployeeEntity employeeEntity = employeeRepository.getById(2L);

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
                () -> employeeService.deleteEmployee(2L)
        );

        assertEquals("Unable to delete employee with id 2", thrown.getMessage());
    }
}