package com.griddynamics.cd.service;

import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.exception.EntityDeleteException;
import com.griddynamics.cd.mapper.DepartmentMapper;
import com.griddynamics.cd.model.Department;
import com.griddynamics.cd.model.create.CreateDepartmentRequest;
import com.griddynamics.cd.model.update.UpdateDepartmentRequest;
import com.griddynamics.cd.repository.DepartmentRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDepartmentModel)
                .toList();
    }

    public ResponseEntity<?> getDepartmentPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Department> page = new PageImpl<>(
                departmentRepository.findAll(pageable).stream()
                        .map(departmentMapper::toDepartmentModel)
                        .toList()
        );

        HashMap<String, Object> values = new HashMap<>();
        values.put("pageNumber", page.getNumber());
        values.put("pageSize", page.getSize());
        values.put("totalPages", page.getTotalPages());
        values.put("totalObjects", page.getTotalElements());
        values.put("cars", page.getContent());

        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    public Department getDepartmentById(Long departmentId) {
        return departmentMapper.toDepartmentModel(
                departmentRepository.findById(departmentId)
                        .orElseThrow(() -> new EntityNotFoundException("Department with " + departmentId + " id was not found"))
        );
    }

    public Department saveDepartment(CreateDepartmentRequest createDepartmentRequest) {
        if (createDepartmentRequest.getEmail() != null) {
            if (departmentRepository.existsByEmail(createDepartmentRequest.getEmail())) {
                throw new EntityExistsException("Department with " + createDepartmentRequest.getEmail() + " email already exist");
            }
        }

        return departmentMapper.toDepartmentModel(
                departmentRepository.save(
                        departmentMapper.toDepartmentEntity(createDepartmentRequest))
        );
    }

    public Department updateDepartment(UpdateDepartmentRequest updateDepartmentRequest, Long departmentId) {
        DepartmentEntity departmentEntity = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department with " + departmentId + " id was not found"));

        if (updateDepartmentRequest.getEmail() != null) {
            if (departmentRepository.existsByEmailAndIdIsNot(updateDepartmentRequest.getEmail(), departmentId)) {
                throw new EntityExistsException("Department with " + updateDepartmentRequest.getEmail() + " email already exist");
            }
        }

        return departmentMapper.toDepartmentModel(
                departmentRepository.save(
                        departmentMapper.toDepartmentEntity(updateDepartmentRequest, departmentEntity)
                )
        );
    }

    public void deleteDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new EntityNotFoundException("Department with " + departmentId + " id was not found");
        }

        if (!employeeRepository.findAllEmployeesByDepartmentId(departmentId).isEmpty()) {
            throw new EntityDeleteException("Unable to delete department with id " + departmentId);
        }

        departmentRepository.deleteById(departmentId);
    }
}
