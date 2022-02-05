package com.griddynamics.cd.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.cd.BaseIntegrationTest;
import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.DepartmentType;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.model.create.CreateEmployeeRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.model.update.UpdateEmployeeRequest;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.DepartmentRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
public class EmployeeControllerTest extends BaseIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
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

        st.execute("TRUNCATE TABLE car, employee, department RESTART IDENTITY;");
        st.close();
    }

    @Test
    void getAllEmployees_whenSaveToEmployeeRepository_thenReturnValidList() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(employees)));
    }

    @Test
    void getDepartmentById_whenPassValidEmployeeIdTwoTimes_thenReturnValidModel() throws Exception {
        mockMvc.perform(get("/employees/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(employees.get(1))));

        mockMvc.perform(get("/employees/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(employees.get(3))));
    }

    @Test
    void getEmployeeById_whenPassInvalidEmployeeId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(get("/employees/123"))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Employee with 123 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void getEmployeesByDepartmentId_whenCallMethodTwoTimes_thenReturnValidListOfEmployeeModels() throws Exception {
        mockMvc.perform(get("/departments/1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(employees.get(0)))));

        mockMvc.perform(get("/departments/2/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(employees.get(1), employees.get(2), employees.get(3)))));
    }

    @Test
    void saveDepartment_whenPassValidCreateDepartmentRequest_thenReturnValidModel() throws Exception {
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

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void saveEmployee_whenPassCreateEmployeeRequestWithExistingPhoneNumber_thenThrowEntityExistsException() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = CreateEmployeeRequest.builder()
                .firstName("Van")
                .lastName("Keefer")
                .phoneNumber("7630894488")
                .build();

        MvcResult result = mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals("Employee with 7630894488 phone number already exist",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void saveEmployee_whenPassCreateCarRequestWithInvalidDepartmentId_thenThrowEntityNotFoundException() throws Exception {
        CreateEmployeeRequest employeeRequest = CreateEmployeeRequest.builder()
                .firstName("Van")
                .lastName("Keefer")
                .departmentId(111L)
                .build();

        MvcResult result = mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Department with 111 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void updateDepartment_whenPassValidUpdateDepartmentRequest_thenReturnValidModel() throws Exception {
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

        mockMvc.perform(put("/employees/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void updateEmployee_whenPassInvalidEmployeeId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(put("/employees/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateCarRequest())))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Employee with 123 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void updateEmployee_whenPassUpdateEmployeeRequestWithExistingPhoneNumber_thenThrowEntityExistsException() throws Exception {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .phoneNumber("7630894488")
                .build();

        MvcResult result = mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals("Employee with 7630894488 phone number already exist",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    @Order(7)
    void updateEmployee_whenPassUpdateEmployeeRequestWithInvalidDepartmentId_thenThrowEntityNotFoundException() throws Exception {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .departmentId(111L)
                .build();

        MvcResult result = mockMvc.perform(put("/employees/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Department with 111 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void deleteDepartmentById_whenPassValidDepartmentId_thenCheckIfEntityActuallyDeleted() throws Exception {
        mockMvc.perform(delete("/employees/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        assertFalse(employeeRepository.existsById(2L));
    }

    @Test
    void deleteEmployeeById_whenPassInvalidEmployeeId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(delete("/employees/114"))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Employee with 114 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void deleteEmployeeById_whenPassEmployeeIdWithDependentCars_thenThrowEntityDeleteException() throws Exception {
        EmployeeEntity employeeEntity = employeeRepository.getById(2L);

        CarEntity carEntity = CarEntity.builder()
                .manufacturer("Audi")
                .model("A2")
                .vinNumber("JH4KA8271NC000480")
                .employee(employeeEntity)
                .color(Color.WHITE)
                .build();
        carRepository.save(carEntity);

        MvcResult result = mockMvc.perform(delete("/employees/2"))
                .andExpect(status().isConflict())
                .andReturn();
        assertEquals("Unable to delete employee with id 2",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }
}
