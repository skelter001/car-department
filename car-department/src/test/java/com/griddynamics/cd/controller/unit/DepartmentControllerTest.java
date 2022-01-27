package com.griddynamics.cd.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.cd.controller.DepartmentController;
import com.griddynamics.cd.model.Department;
import com.griddynamics.cd.model.DepartmentType;
import com.griddynamics.cd.model.create.CreateDepartmentRequest;
import com.griddynamics.cd.model.update.UpdateDepartmentRequest;
import com.griddynamics.cd.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class DepartmentControllerTest {

    private final DepartmentService departmentService = mock(DepartmentService.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(departmentService)).build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllDepartments_whenCallMethod_thenReturnOk() throws Exception {
        when(departmentService.getAllDepartments())
                .thenReturn(List.of(mock(Department.class)));

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void getDepartmentById_whenPassValidId_thenReturnOk() throws Exception {
        when(departmentService.getDepartmentById(1L))
                .thenReturn(mock(Department.class));

        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void saveDepartment_whenValidCreateDepartmentRequest_thenReturnOk() throws Exception {
        CreateDepartmentRequest createDepartmentRequest = mock(CreateDepartmentRequest.class);

        when(createDepartmentRequest.getName())
                .thenReturn("Department 1");
        when(createDepartmentRequest.getEmail())
                .thenReturn("test@test");
        when(createDepartmentRequest.getDescription())
                .thenReturn("some desc.");
        when(createDepartmentRequest.getDepartmentType())
                .thenReturn(mock(DepartmentType.class));

        when(departmentService.saveDepartment(any(CreateDepartmentRequest.class)))
                .thenReturn(mock(Department.class));

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDepartmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void saveDepartment_whenCreateDepartmentRequestWithInvalidName_thenReturnBadRequest() throws Exception {
        CreateDepartmentRequest createDepartmentRequest = mock(CreateDepartmentRequest.class);

        when(createDepartmentRequest.getName())
                .thenReturn("De@#!");
        when(createDepartmentRequest.getDepartmentType())
                .thenReturn(mock(DepartmentType.class));

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDepartmentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveDepartment_whenCreateDepartmentRequestWithInvalidEmail_thenReturnBadRequest() throws Exception {
        CreateDepartmentRequest createDepartmentRequest = mock(CreateDepartmentRequest.class);

        when(createDepartmentRequest.getEmail())
                .thenReturn("wrong");
        when(createDepartmentRequest.getDepartmentType())
                .thenReturn(mock(DepartmentType.class));

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDepartmentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveDepartment_whenCreateDepartmentRequestWithoutDepartmentType_thenReturnBadRequest() throws Exception {
        CreateDepartmentRequest createDepartmentRequest = mock(CreateDepartmentRequest.class);

        when(createDepartmentRequest.getEmail())
                .thenReturn("test@test");

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDepartmentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDepartment_whenValidUpdateDepartmentRequest_thenReturnOk() throws Exception {
        UpdateDepartmentRequest updateDepartmentRequest = mock(UpdateDepartmentRequest.class);

        when(updateDepartmentRequest.getName())
                .thenReturn("Department 1");
        when(updateDepartmentRequest.getEmail())
                .thenReturn("test@test");
        when(updateDepartmentRequest.getDescription())
                .thenReturn("some desc.");
        when(updateDepartmentRequest.getDepartmentType())
                .thenReturn(mock(DepartmentType.class));

        when(departmentService.updateDepartment(any(UpdateDepartmentRequest.class), anyLong()))
                .thenReturn(mock(Department.class));

        mockMvc.perform(put("/departments/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDepartmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void updateDepartment_whenUpdateDepartmentRequestWithInvalidName_thenReturnBadRequest() throws Exception {
        UpdateDepartmentRequest updateDepartmentRequest = mock(UpdateDepartmentRequest.class);

        when(updateDepartmentRequest.getName())
                .thenReturn("De@#!");
        when(updateDepartmentRequest.getDepartmentType())
                .thenReturn(mock(DepartmentType.class));

        mockMvc.perform(put("/departments/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDepartmentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDepartment_whenUpdateDepartmentRequestWithInvalidEmail_thenReturnBadRequest() throws Exception {
        UpdateDepartmentRequest updateDepartmentRequest = mock(UpdateDepartmentRequest.class);

        when(updateDepartmentRequest.getEmail())
                .thenReturn("wrong");
        when(updateDepartmentRequest.getDepartmentType())
                .thenReturn(mock(DepartmentType.class));

        mockMvc.perform(put("/departments/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDepartmentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteDepartmentById_whenPassValidId_thenReturnOk() throws Exception {
        doNothing().when(departmentService)
                .deleteDepartment(anyLong());

        mockMvc.perform(delete("/departments/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
