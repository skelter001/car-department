package com.griddynamics.cd.service.unit;

import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.exception.EntityDeleteException;
import com.griddynamics.cd.mapper.EmployeeMapper;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.model.create.CreateEmployeeRequest;
import com.griddynamics.cd.model.update.UpdateEmployeeRequest;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.DepartmentRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import com.griddynamics.cd.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;
    private DepartmentRepository departmentRepository;
    private CarRepository carRepository;
    private EmployeeService employeeService;

    @BeforeEach
    void init() {
        employeeRepository = mock(EmployeeRepository.class);
        employeeMapper = mock(EmployeeMapper.class);
        departmentRepository = mock(DepartmentRepository.class);
        carRepository = mock(CarRepository.class);
        employeeService = new EmployeeService(employeeRepository, employeeMapper, departmentRepository, carRepository);
    }

    @BeforeEach
    void setUp() {
        when(employeeRepository.save(any(EmployeeEntity.class)))
                .thenReturn(mock(EmployeeEntity.class));

        when(employeeRepository.findById(anyLong()))
                .thenReturn(Optional.of(mock(EmployeeEntity.class)));

        when(employeeMapper.toEmployeeEntity(any(CreateEmployeeRequest.class)))
                .thenReturn(mock(EmployeeEntity.class));

        when(employeeMapper.toEmployeeModel(any(EmployeeEntity.class)))
                .thenReturn(mock(Employee.class));

        when(employeeMapper.toEmployeeEntity(any(UpdateEmployeeRequest.class), any(EmployeeEntity.class)))
                .thenReturn(mock(EmployeeEntity.class));

        when(departmentRepository.findById(anyLong()))
                .thenReturn(Optional.of(mock(DepartmentEntity.class)));
    }

    @Test
    void getAllEmployees_whenCallMethod_thenValidMethodCallsNumber() {
        EmployeeEntity employeeEntity1 = mock(EmployeeEntity.class);
        EmployeeEntity employeeEntity2 = mock(EmployeeEntity.class);

        when(employeeRepository.findAll())
                .thenReturn(List.of(employeeEntity1, employeeEntity2));

        employeeService.getAllEmployees();

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeById_whenPassId_thenValidMethodCallsNumber() {
        employeeService.getEmployeeById(1L);
        employeeService.getEmployeeById(1L);
        employeeService.getEmployeeById(2L);

        verify(employeeRepository, times(2)).findById(1L);
        verify(employeeRepository, times(1)).findById(2L);
        verify(employeeMapper, times(3)).toEmployeeModel(any(EmployeeEntity.class));
    }

    @Test
    void getEmployeeById_whenPassWrongId_thenThrowEntityNotFoundException() {
        when(employeeRepository.findById(12L))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                employeeService.getEmployeeById(12L));
        assertEquals(thrown.getMessage(), "Employee with 12 id was not found");
    }

    @Test
    void getEmployeeByDepartmentId_whenPassEmployeeId_thenValidMethodCallsNumber() {
        when(employeeRepository.findAllEmployeesByDepartmentId(5L))
                .thenReturn(List.of(mock(EmployeeEntity.class), mock(EmployeeEntity.class), mock(EmployeeEntity.class)));

        employeeService.getEmployeesByDepartmentId(5L);

        verify(employeeRepository, times(1)).findAllEmployeesByDepartmentId(5L);
        verify(employeeMapper, times(3)).toEmployeeModel(any(EmployeeEntity.class));
    }

    @Test
    void saveEmployee_whenSaveEmployeeRequestWithoutDepartmentId_thenValidMethodCallsNumber() {
        CreateEmployeeRequest createEmployeeRequest = mock(CreateEmployeeRequest.class);
        when(createEmployeeRequest.getDepartmentId())
                .thenReturn(null);

        employeeService.saveEmployee(createEmployeeRequest);

        verify(employeeMapper, times(1)).toEmployeeModel(any(EmployeeEntity.class));
        verify(employeeMapper, times(1)).toEmployeeEntity(any(CreateEmployeeRequest.class));
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    void saveEmployee_whenSaveEmployeeRequestWithDepartmentId_thenValidMethodCallsNumber() {
        CreateEmployeeRequest createEmployeeRequest = mock(CreateEmployeeRequest.class);
        when(createEmployeeRequest.getDepartmentId())
                .thenReturn(2L);

        employeeService.saveEmployee(createEmployeeRequest);

        verify(departmentRepository, times(1)).findById(2L);
        verify(employeeMapper, times(1)).toEmployeeModel(any(EmployeeEntity.class));
        verify(employeeMapper, times(1)).toEmployeeEntity(any(CreateEmployeeRequest.class));
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    void saveEmployee_whenSaveDifferentEmployeeRequests_thenValidMethodCallsNumber() {
        CreateEmployeeRequest createEmployeeRequest1 = mock(CreateEmployeeRequest.class);
        when(createEmployeeRequest1.getDepartmentId())
                .thenReturn(null);
        CreateEmployeeRequest createEmployeeRequest2 = mock(CreateEmployeeRequest.class);
        when(createEmployeeRequest2.getDepartmentId())
                .thenReturn(2L);

        employeeService.saveEmployee(createEmployeeRequest1);
        employeeService.saveEmployee(createEmployeeRequest2);

        verify(departmentRepository, times(1)).findById(2L);
        verify(employeeMapper, times(2)).toEmployeeModel(any(EmployeeEntity.class));
        verify(employeeMapper, times(2)).toEmployeeEntity(any(CreateEmployeeRequest.class));
        verify(employeeRepository, times(2)).save(any(EmployeeEntity.class));
    }

    @Test
    void saveEmployee_whenSaveEmployeeWithExistingPhoneNumber_thenThrowEntityExistsException() {
        CreateEmployeeRequest createEmployeeRequest = mock(CreateEmployeeRequest.class);
        when(createEmployeeRequest.getPhoneNumber())
                .thenReturn("1234567890");
        when(employeeRepository.existsByPhoneNumber("1234567890"))
                .thenReturn(true);

        EntityExistsException thrown = assertThrows(EntityExistsException.class, () ->
                employeeService.saveEmployee(createEmployeeRequest));

        assertEquals("Employee with 1234567890 phone number already exist", thrown.getMessage());

    }

    @Test
    void saveEmployee_whenPassWrongDepartmentId_thenThrowEntityNotFoundException() {
        CreateEmployeeRequest createEmployeeRequest = mock(CreateEmployeeRequest.class);
        when(createEmployeeRequest.getDepartmentId())
                .thenReturn(3L);
        when(departmentRepository.findById(3L))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                employeeService.saveEmployee(createEmployeeRequest));

        assertEquals("Department with 3 id was not found", thrown.getMessage());
    }

    @Test
    void updateEmployee_whenUpdateRequestWithoutDepartmentId_thenValidMethodCallsNumber() {
        UpdateEmployeeRequest updateEmployeeRequest = mock(UpdateEmployeeRequest.class);
        when(updateEmployeeRequest.getDepartmentId())
                .thenReturn(null);

        employeeService.updateEmployee(updateEmployeeRequest, 2L);

        verify(employeeRepository, times(1)).findById(2L);
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
        verify(employeeMapper, times(1)).toEmployeeModel(any(EmployeeEntity.class));
        verify(employeeMapper, times(1)).toEmployeeEntity(any(UpdateEmployeeRequest.class), any(EmployeeEntity.class));
    }

    @Test
    void updateEmployee_whenUpdateRequestWithDepartmentId_thenValidMethodCallsNumber() {
        UpdateEmployeeRequest updateEmployeeRequest = mock(UpdateEmployeeRequest.class);
        when(updateEmployeeRequest.getDepartmentId())
                .thenReturn(1L);

        employeeService.updateEmployee(updateEmployeeRequest, 2L);

        verify(departmentRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findById(2L);
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
        verify(employeeMapper, times(1)).toEmployeeModel(any(EmployeeEntity.class));
        verify(employeeMapper, times(1)).toEmployeeEntity(any(UpdateEmployeeRequest.class), any(EmployeeEntity.class));
    }

    @Test
    void updateEmployee_whenPassWrongEmployeeId_thenThrowEntityNotFoundException() {
        when(employeeRepository.findById(2L))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                employeeService.updateEmployee(mock(UpdateEmployeeRequest.class), 2L));
        assertEquals("Employee with 2 id was not found", thrown.getMessage());
    }

    @Test
    void updateEmployee_whenPassEmployeeWithExistingPhoneNumber_thenThrowEntityExistsException() {
        UpdateEmployeeRequest updateEmployeeRequest = mock(UpdateEmployeeRequest.class);
        when(updateEmployeeRequest.getPhoneNumber())
                .thenReturn("1234567890");
        when(employeeRepository.existsByPhoneNumberAndIdIsNot("1234567890", 1L))
                .thenReturn(true);

        EntityExistsException thrown = assertThrows(EntityExistsException.class, () ->
                employeeService.updateEmployee(updateEmployeeRequest, 1L));
        assertEquals("Employee with 1234567890 phone number already exist", thrown.getMessage());
    }

    @Test
    void updateEmployee_whenPassUpdateRequestWithWrongDepartmentId_thenThrowEntityNotFoundException() {
        UpdateEmployeeRequest updateEmployeeRequest = mock(UpdateEmployeeRequest.class);
        when(updateEmployeeRequest.getDepartmentId())
                .thenReturn(1L);
        when(departmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                employeeService.updateEmployee(updateEmployeeRequest, 2L));
        assertEquals("Department with 1 id was not found", thrown.getMessage());
    }

    @Test
    void deleteEmployee_whenDeleteById_thenPassValidValue() {
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        doNothing().when(employeeRepository)
                .deleteById(captor.capture());

        when(employeeRepository.existsById(3L))
                .thenReturn(true);

        employeeService.deleteEmployee(3L);

        verify(employeeRepository, times(1)).deleteById(3L);
        assertEquals(3L, captor.getValue());
    }

    @Test
    void deleteEmployee_whenPassWrongEmployeeId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                employeeService.deleteEmployee(3L));
        assertEquals("Employee with 3 id was not found", thrown.getMessage());
    }

    @Test
    void deleteEmployee_whenPassEmployeeIdWithDependentCars_thenThrowEntityNotFoundException() {
        when(employeeRepository.existsById(3L))
                .thenReturn(true);
        when(carRepository.findAllCarsByEmployeeId(3L))
                .thenReturn(List.of(mock(CarEntity.class)));

        EntityDeleteException thrown = assertThrows(EntityDeleteException.class, () ->
                employeeService.deleteEmployee(3L));
        assertEquals("Unable to delete department with id 3", thrown.getMessage());
    }
}