package com.griddynamics.cd.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.cd.BaseIntegrationTest;
import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.model.Department;
import com.griddynamics.cd.model.DepartmentType;
import com.griddynamics.cd.model.create.CreateDepartmentRequest;
import com.griddynamics.cd.model.update.UpdateCarRequest;
import com.griddynamics.cd.model.update.UpdateDepartmentRequest;
import com.griddynamics.cd.repository.DepartmentRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class DepartmentControllerTest extends BaseIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private final List<Department> departments = List.of(
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
    void cleanUp() throws SQLException {
        Statement st = connection.createStatement();

        st.execute("TRUNCATE TABLE employee RESTART IDENTITY CASCADE ;");
        st.execute("TRUNCATE TABLE department RESTART IDENTITY CASCADE ;");
        st.close();
    }

    @Test
    void getAllDepartments_whenSaveToDepartmentRepository_thenReturnValidList() throws Exception {
        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(departments)));
    }

    @Test
    void getDepartmentById_whenPassValidIdTwoTimes_thenReturnValidModel() throws Exception {
        mockMvc.perform(get("/departments/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(departments.get(1))));

        mockMvc.perform(get("/departments/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(departments.get(3))));
    }

    @Test
    void getDepartmentById_whenPassInvalidDepartmentId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(get("/departments/100"))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Department with 100 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void saveDepartment_whenPassValidCreateDepartmentRequest_thenReturnValidModel() throws Exception {
        CreateDepartmentRequest createDepartmentRequest = CreateDepartmentRequest.builder()
                .name("department")
                .email("dep@dep")
                .description("smth")
                .departmentType(DepartmentType.PROVIDER)
                .build();

        Department expected = Department.builder()
                .id(5L)
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
    void saveDepartment_whenPassCreateDepartmentRequestWithExistingEmail_thenThrowEntityExistsException() throws Exception {
        CreateDepartmentRequest createDepartmentRequest = CreateDepartmentRequest.builder()
                .name("department")
                .email("test1@test")
                .description("smth")
                .departmentType(DepartmentType.PROVIDER)
                .build();

        MvcResult result = mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDepartmentRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals("Department with test1@test email already exist",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void updateDepartment_whenPassValidUpdateDepartmentRequest_thenReturnValidModel() throws Exception {
        UpdateDepartmentRequest updateDepartmentRequest = UpdateDepartmentRequest.builder()
                .name("new name")
                .email("new@mail")
                .description("new desc")
                .departmentType(DepartmentType.SUPPORT)
                .build();

        Department expected = Department.builder()
                .id(1L)
                .name("new name")
                .email("new@mail")
                .description("new desc")
                .departmentType(DepartmentType.SUPPORT)
                .build();

        mockMvc.perform(put("/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDepartmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void updateDepartment_whenPassWrongDepartmentId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(put("/departments/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateCarRequest())))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Department with 123 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void updateDepartment_whenPassUpdateDepartmentRequestWithExistingEmail_thenThrowEntityExistsException() throws Exception {
        UpdateDepartmentRequest updateDepartmentRequest = UpdateDepartmentRequest.builder()
                .name("new name")
                .email("test1@test")
                .description("new desc")
                .departmentType(DepartmentType.SUPPORT)
                .build();

        MvcResult result = mockMvc.perform(put("/departments/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDepartmentRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals("Department with test1@test email already exist",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void deleteDepartment_whenPassValidDepartmentId_thenCheckIfEntityActuallyDeleted() throws Exception {
        mockMvc.perform(delete("/departments/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        assertFalse(departmentRepository.existsById(2L));
    }

    @Test
    void deleteDepartment_whenPassInvalidDepartmentId_thenThrowEntityNotFoundException() throws Exception {
        MvcResult result = mockMvc.perform(delete("/departments/114"))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Department with 114 id was not found",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    void deleteDepartment_whenPasDepartmentIdWithDependentEmployees_thenThrowEntityDeleteException() throws Exception {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .firstName("Joe")
                .lastName("Doe")
                .department(departmentRepository.getById(3L))
                .build();
        employeeRepository.save(employeeEntity);

        MvcResult result = mockMvc.perform(delete("/departments/3"))
                .andExpect(status().isConflict())
                .andReturn();
        assertEquals("Unable to delete department with id 3",
                Objects.requireNonNull(result.getResolvedException()).getMessage());
    }
}
