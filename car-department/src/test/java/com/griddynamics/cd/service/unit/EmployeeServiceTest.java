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
        employeeService = new EmployeeService(employeeRepository, departmentRepository, carRepository, employeeMapper);
    }

    @BeforeEach
    void setUp() {
        when(employeeRepository.save(any(EmployeeEntity.class)))
                .thenReturn(new EmployeeEntity());

        when(employeeRepository.findById(anyLong()))
                .thenReturn(Optional.of(new EmployeeEntity()));

        when(employeeMapper.toEmployeeEntity(any(CreateEmployeeRequest.class)))
                .thenReturn(new EmployeeEntity());

        when(employeeMapper.toEmployeeModel(any(EmployeeEntity.class)))
                .thenReturn(new Employee());

        when(employeeMapper.toEmployeeEntity(any(UpdateEmployeeRequest.class), any(EmployeeEntity.class)))
                .thenReturn(new EmployeeEntity());

        when(departmentRepository.findById(anyLong()))
                .thenReturn(Optional.of(new DepartmentEntity()));
    }

    @Test
    void getAllEmployees_whenCallMethod_thenValidMethodCallsNumber() {
        when(employeeRepository.findAll())
                .thenReturn(List.of(new EmployeeEntity(), new EmployeeEntity()));

        employeeService.getAllEmployees();

        verify(employeeRepository, times(1)).findAll();
        verify(employeeMapper, times(2)).toEmployeeModel(any(EmployeeEntity.class));
    }

    @Test
    void getEmployeeById_whenPassEmployeeId_thenValidMethodCallsNumber() {
        employeeService.getEmployeeById(1L);
        employeeService.getEmployeeById(1L);
        employeeService.getEmployeeById(2L);

        verify(employeeRepository, times(2)).findById(1L);
        verify(employeeRepository, times(1)).findById(2L);
        verify(employeeMapper, times(3)).toEmployeeModel(any(EmployeeEntity.class));
    }

    @Test
    void getEmployeeById_whenPassInvalidEmployeeId_thenThrowEntityNotFoundException() {
        when(employeeRepository.findById(12L))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.getEmployeeById(12L)
        );
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
    void saveEmployee_whenPassCreateEmployeeRequestWithoutDepartmentId_thenValidMethodCallsNumber() {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();

        employeeService.saveEmployee(createEmployeeRequest);

        verify(employeeMapper, times(1)).toEmployeeModel(any(EmployeeEntity.class));
        verify(employeeMapper, times(1)).toEmployeeEntity(createEmployeeRequest);
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    void saveEmployee_whenPassCreateEmployeeRequestWithDepartmentId_thenValidMethodCallsNumber() {
        CreateEmployeeRequest createEmployeeRequest = CreateEmployeeRequest.builder()
                .departmentId(2L)
                .build();

        employeeService.saveEmployee(createEmployeeRequest);

        verify(departmentRepository, times(1)).findById(2L);
        verify(employeeMapper, times(1)).toEmployeeModel(any(EmployeeEntity.class));
        verify(employeeMapper, times(1)).toEmployeeEntity(createEmployeeRequest);
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    void saveEmployee_whenPassDifferentCreateEmployeeRequests_thenValidMethodCallsNumber() {
        CreateEmployeeRequest createEmployeeRequest1 = new CreateEmployeeRequest();
        CreateEmployeeRequest createEmployeeRequest2 = CreateEmployeeRequest.builder()
                .departmentId(2L)
                .build();

        employeeService.saveEmployee(createEmployeeRequest1);
        employeeService.saveEmployee(createEmployeeRequest2);

        verify(departmentRepository, times(1)).findById(2L);
        verify(employeeMapper, times(2)).toEmployeeModel(any(EmployeeEntity.class));
        verify(employeeMapper, times(2)).toEmployeeEntity(any(CreateEmployeeRequest.class));
        verify(employeeRepository, times(2)).save(any(EmployeeEntity.class));
    }

    @Test
    void saveEmployee_whenPassCreateEmployeeRequestWithExistingPhoneNumber_thenThrowEntityExistsException() {
        CreateEmployeeRequest createEmployeeRequest = CreateEmployeeRequest.builder()
                .phoneNumber("1234567890")
                .build();
        when(employeeRepository.existsByPhoneNumber("1234567890"))
                .thenReturn(true);

        EntityExistsException thrown = assertThrows(
                EntityExistsException.class,
                () -> employeeService.saveEmployee(createEmployeeRequest)
        );

        assertEquals("Employee with 1234567890 phone number already exist", thrown.getMessage());
    }

    @Test
    void saveEmployee_whenPassInvalidDepartmentId_thenThrowEntityNotFoundException() {
        CreateEmployeeRequest createEmployeeRequest = CreateEmployeeRequest.builder()
                .departmentId(3L)
                .build();

        when(departmentRepository.findById(3L))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.saveEmployee(createEmployeeRequest)
        );

        assertEquals("Department with 3 id was not found", thrown.getMessage());
    }

    @Test
    void updateEmployee_whenUpdateEmployeeRequestWithoutDepartmentId_thenValidMethodCallsNumber() {
        UpdateEmployeeRequest updateEmployeeRequest = new UpdateEmployeeRequest();

        employeeService.updateEmployee(updateEmployeeRequest, 2L);

        verify(employeeRepository, times(1)).findById(2L);
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
        verify(employeeMapper, times(1)).toEmployeeModel(any(EmployeeEntity.class));
        verify(employeeMapper, times(1)).toEmployeeEntity(eq(updateEmployeeRequest), any(EmployeeEntity.class));
    }

    @Test
    void updateEmployee_whenUpdateEmployeeRequestWithDepartmentId_thenValidMethodCallsNumber() {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .departmentId(1L)
                .build();

        employeeService.updateEmployee(updateEmployeeRequest, 2L);

        verify(departmentRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findById(2L);
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
        verify(employeeMapper, times(1)).toEmployeeModel(any(EmployeeEntity.class));
        verify(employeeMapper, times(1)).toEmployeeEntity(eq(updateEmployeeRequest), any(EmployeeEntity.class));
    }

    @Test
    void updateEmployee_whenPassInvalidEmployeeId_thenThrowEntityNotFoundException() {
        when(employeeRepository.findById(2L))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.updateEmployee(new UpdateEmployeeRequest(), 2L)
        );
        assertEquals("Employee with 2 id was not found", thrown.getMessage());
    }

    @Test
    void updateEmployee_whenPassUpdateEmployeeRequestWithExistingPhoneNumber_thenThrowEntityExistsException() {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .phoneNumber("1234567890")
                .build();

        when(employeeRepository.existsByPhoneNumberAndIdIsNot("1234567890", 1L))
                .thenReturn(true);

        EntityExistsException thrown = assertThrows(
                EntityExistsException.class,
                () -> employeeService.updateEmployee(updateEmployeeRequest, 1L)
        );
        assertEquals("Employee with 1234567890 phone number already exist", thrown.getMessage());
    }

    @Test
    void updateEmployee_whenPassUpdateEmployeeRequestWithInvalidDepartmentId_thenThrowEntityNotFoundException() {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .departmentId(1L)
                .build();

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.updateEmployee(updateEmployeeRequest, 2L)
        );
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
    void deleteEmployee_whenPassInvalidEmployeeId_thenThrowEntityNotFoundException() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.deleteEmployee(3L)
        );
        assertEquals("Employee with 3 id was not found", thrown.getMessage());
    }

    @Test
    void deleteEmployee_whenPassEmployeeIdWithDependentCars_thenThrowEntityDeleteException() {
        when(employeeRepository.existsById(3L))
                .thenReturn(true);
        when(carRepository.findAllCarsByEmployeeId(3L))
                .thenReturn(List.of(mock(CarEntity.class)));

        EntityDeleteException thrown = assertThrows(
                EntityDeleteException.class,
                () -> employeeService.deleteEmployee(3L)
        );
        assertEquals("Unable to delete employee with id 3", thrown.getMessage());
    }
}
