package com.griddynamics.cd.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.griddynamics.cd.controller.EmployeeController;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.model.create.CreateEmployeeRequest;
import com.griddynamics.cd.model.update.UpdateEmployeeRequest;
import com.griddynamics.cd.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

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
        when(employeeService.getAllEmployees())
                .thenReturn(List.of(mock(Employee.class)));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void getEmployeeById_whenPassValidId_thenReturnOk() throws Exception {
        when(employeeService.getEmployeeById(12L))
                .thenReturn(mock(Employee.class));

        mockMvc.perform(get("/employees/12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void getEmployeesByDepartmentId_whenPassValidDepartmentId_thenReturnOk() throws Exception {
        when(employeeService.getEmployeesByDepartmentId(3L))
                .thenReturn(List.of(mock(Employee.class), mock(Employee.class)));

        mockMvc.perform(get("/departments/3/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void saveEmployee_whenValidCreateEmployeeRequest_thenReturnOk() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = mock(CreateEmployeeRequest.class);

        when(createEmployeeRequest.getFirstName())
                .thenReturn("Joe");
        when(createEmployeeRequest.getLastName())
                .thenReturn("Doe");
        when(createEmployeeRequest.getDepartmentId())
                .thenReturn(2L);
        when(createEmployeeRequest.getBirthday())
                .thenReturn(LocalDate.of(1998, 10, 10));
        when(createEmployeeRequest.getAddress())
                .thenReturn("Dallas, Texas US");
        when(createEmployeeRequest.getPhoneNumber())
                .thenReturn("1234567890");

        when(employeeService.saveEmployee(any(CreateEmployeeRequest.class)))
                .thenReturn(mock(Employee.class));

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void saveEmployee_whenCreateEmployeeRequestWithoutFirstName_thenReturnBadRequest() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = mock(CreateEmployeeRequest.class);

        when(createEmployeeRequest.getLastName())
                .thenReturn("Doe");
        when(createEmployeeRequest.getDepartmentId())
                .thenReturn(1L);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveEmployee_whenCreateEmployeeRequestWithInvalidBirthday_thenReturnBadRequest() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = mock(CreateEmployeeRequest.class);

        when(createEmployeeRequest.getFirstName())
                .thenReturn("Joe");
        when(createEmployeeRequest.getLastName())
                .thenReturn("Doe");
        when(createEmployeeRequest.getDepartmentId())
                .thenReturn(1L);
        when(createEmployeeRequest.getBirthday())
                .thenReturn(LocalDate.now());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveEmployee_whenCreateEmployeeRequestWithInvalidPhoneNumber_thenReturnBadRequest() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = mock(CreateEmployeeRequest.class);

        when(createEmployeeRequest.getFirstName())
                .thenReturn("Joe");
        when(createEmployeeRequest.getLastName())
                .thenReturn("Doe");
        when(createEmployeeRequest.getDepartmentId())
                .thenReturn(1L);
        when(createEmployeeRequest.getPhoneNumber())
                .thenReturn("wrong");

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEmployee_whenValidUpdateEmployeeRequest_thenReturnOk() throws Exception {
        UpdateEmployeeRequest updateEmployeeRequest = mock(UpdateEmployeeRequest.class);

        when(updateEmployeeRequest.getFirstName())
                .thenReturn("Joe");
        when(updateEmployeeRequest.getLastName())
                .thenReturn("Doe");
        when(updateEmployeeRequest.getDepartmentId())
                .thenReturn(2L);
        when(updateEmployeeRequest.getBirthday())
                .thenReturn(LocalDate.of(1998, 10, 10));
        when(updateEmployeeRequest.getAddress())
                .thenReturn("Dallas, Texas US");
        when(updateEmployeeRequest.getPhoneNumber())
                .thenReturn("1234567890");

        when(employeeService.updateEmployee(any(UpdateEmployeeRequest.class), anyLong()))
                .thenReturn(mock(Employee.class));

        mockMvc.perform(put("/employees/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void updateEmployee_whenUpdateEmployeeRequestWithoutLastName_thenReturnBadRequest() throws Exception {
        UpdateEmployeeRequest updateEmployeeRequest = mock(UpdateEmployeeRequest.class);

        when(updateEmployeeRequest.getFirstName())
                .thenReturn("Joe");
        when(updateEmployeeRequest.getDepartmentId())
                .thenReturn(12L);

        mockMvc.perform(put("/employees/123L")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEmployee_whenUpdateEmployeeRequestWithInvalidBirthday_thenReturnBadRequest() throws Exception {
        UpdateEmployeeRequest updateEmployeeRequest = mock(UpdateEmployeeRequest.class);

        when(updateEmployeeRequest.getFirstName())
                .thenReturn("Joe");
        when(updateEmployeeRequest.getLastName())
                .thenReturn("Doe");
        when(updateEmployeeRequest.getDepartmentId())
                .thenReturn(1L);
        when(updateEmployeeRequest.getBirthday())
                .thenReturn(LocalDate.now());

        mockMvc.perform(put("/employees/123L")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEmployee_whenUpdateEmployeeRequestWithInvalidPhoneNumber_thenReturnBadRequest() throws Exception {
        UpdateEmployeeRequest updateEmployeeRequest = mock(UpdateEmployeeRequest.class);

        when(updateEmployeeRequest.getFirstName())
                .thenReturn("Joe");
        when(updateEmployeeRequest.getLastName())
                .thenReturn("Doe");
        when(updateEmployeeRequest.getDepartmentId())
                .thenReturn(1L);
        when(updateEmployeeRequest.getPhoneNumber())
                .thenReturn("wrong");

        mockMvc.perform(put("/employees/123L")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteEmployeeById_whenPassValidId_thenReturnOk() throws Exception {
        doNothing().when(employeeService)
                .deleteEmployee(anyLong());

        mockMvc.perform(delete("/employees/-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
