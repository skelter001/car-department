package com.griddynamics.cd.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.griddynamics.cd.controller.EmployeeController;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.model.create.CreateEmployeeRequest;
import com.griddynamics.cd.model.update.UpdateEmployeeRequest;
import com.griddynamics.cd.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerTest {

    private final EmployeeService employeeService = mock(EmployeeService.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new EmployeeController(employeeService)).build();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void getAllEmployees_whenCallMethod_thenReturnOk() throws Exception {
        when(employeeService.getEmployeesWithFiltering(
                anyList(),
                anyList(),
                anyList(),
                anyList(),
                anyList(),
                anyList(),
                anyInt(),
                anyInt(),
                anyString(),
                any(Sort.Direction.class)
        ))
                .thenReturn(ResponseEntity.of(Optional.empty()));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keyToNull").doesNotExist());
    }

    @Test
    void getEmployeeById_whenPassValidId_thenReturnOk() throws Exception {
        when(employeeService.getEmployeeById(12L))
                .thenReturn(new Employee());

        mockMvc.perform(get("/employees/12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void getEmployeesByDepartmentId_whenPassValidDepartmentId_thenReturnOk() throws Exception {
        when(employeeService.getEmployeesByDepartmentId(3L))
                .thenReturn(List.of(new Employee(), new Employee()));

        mockMvc.perform(get("/departments/3/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void saveEmployee_whenValidCreateEmployeeRequest_thenReturnOk() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = CreateEmployeeRequest.builder()
                .firstName("Joe")
                .lastName("Doe")
                .departmentId(2L)
                .birthday(LocalDate.of(1998, 10, 10))
                .address("Dallas, Texas US")
                .phoneNumber("1234567890")
                .build();

        when(employeeService.saveEmployee(any(CreateEmployeeRequest.class)))
                .thenReturn(new Employee());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void saveEmployee_whenCreateEmployeeRequestWithoutFirstName_thenReturnBadRequest() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = CreateEmployeeRequest.builder()
                .lastName("Doe")
                .departmentId(1L)
                .build();

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveEmployee_whenCreateEmployeeRequestWithInvalidBirthday_thenReturnBadRequest() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = CreateEmployeeRequest.builder()
                .firstName("Joe")
                .lastName("Doe")
                .departmentId(1L)
                .birthday(LocalDate.now())
                .build();

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveEmployee_whenCreateEmployeeRequestWithInvalidPhoneNumber_thenReturnBadRequest() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = CreateEmployeeRequest.builder()
                .firstName("Joe")
                .lastName("Doe")
                .departmentId(1L)
                .phoneNumber("wrong")
                .build();

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEmployee_whenValidUpdateEmployeeRequest_thenReturnOk() throws Exception {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .firstName("Joe")
                .lastName("Doe")
                .departmentId(2L)
                .birthday(LocalDate.of(1998, 10, 10))
                .address("Austin, Texas US")
                .phoneNumber("1234567890")
                .build();

        when(employeeService.updateEmployee(any(UpdateEmployeeRequest.class), anyLong()))
                .thenReturn(new Employee());

        mockMvc.perform(put("/employees/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void updateEmployee_whenUpdateEmployeeRequestWithInvalidBirthday_thenReturnBadRequest() throws Exception {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .firstName("Joe")
                .lastName("Doe")
                .departmentId(1L)
                .birthday(LocalDate.now())
                .build();

        mockMvc.perform(put("/employees/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEmployee_whenUpdateEmployeeRequestWithInvalidPhoneNumber_thenReturnBadRequest() throws Exception {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .firstName("Joe")
                .lastName("Doe")
                .departmentId(1L)
                .phoneNumber("wrong")
                .build();

        mockMvc.perform(put("/employees/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteEmployeeById_whenPassValidId_thenReturnOk() throws Exception {
        doNothing().when(employeeService)
                .deleteEmployee(anyLong());

        mockMvc.perform(delete("/employees/21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
