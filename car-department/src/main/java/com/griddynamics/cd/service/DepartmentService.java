package com.griddynamics.cd.service;

import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.exception.ColumnNotFoundException;
import com.griddynamics.cd.exception.EntityDeleteException;
import com.griddynamics.cd.mapper.DepartmentMapper;
import com.griddynamics.cd.model.Department;
import com.griddynamics.cd.model.DepartmentType;
import com.griddynamics.cd.model.create.CreateDepartmentRequest;
import com.griddynamics.cd.model.update.UpdateDepartmentRequest;
import com.griddynamics.cd.repository.DepartmentRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.AllArgsConstructor;
import org.hibernate.jpa.TypedParameterValue;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;

    public ResponseEntity<?> getAllDepartments(List<String> names,
                                               List<String> emails,
                                               List<String> descriptions,
                                               List<DepartmentType> departmentTypes,
                                               int pageNumber,
                                               int pageSize,
                                               String orderBy,
                                               Sort.Direction order) {
        if (!departmentRepository.existsByColumnName(orderBy)) {
            throw new ColumnNotFoundException(orderBy);
        }

        Page<Department> page = new PageImpl<>(departmentRepository.findAllByFilterParamsAndSortAndPaged(
                        new TypedParameterValue(StringArrayType.INSTANCE,
                                Optional.ofNullable(names).map(list -> list.toArray(String[]::new)).orElse(null)),
                        new TypedParameterValue(StringArrayType.INSTANCE,
                                Optional.ofNullable(emails).map(list -> list.toArray(String[]::new)).orElse(null)),
                        new TypedParameterValue(StringArrayType.INSTANCE,
                                Optional.ofNullable(descriptions).map(list -> list.toArray(String[]::new)).orElse(null)),
                        new TypedParameterValue(StringArrayType.INSTANCE,
                                Optional.ofNullable(departmentTypes).map(list -> list.stream()
                                                .map(DepartmentType::name)
                                                .toArray(String[]::new))
                                        .orElse(null)),
                        PageRequest.of(pageNumber, pageSize, Sort.by(order, orderBy))).stream()
                .map(departmentMapper::toDepartmentModel)
                .toList()
        );

        if (page.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        HashMap<String, Object> values = new HashMap<>();
        values.put("pageNumber", page.getNumber());
        values.put("pageSize", page.getSize());
        values.put("totalPages", page.getTotalPages());
        values.put("totalObjects", page.getTotalElements());
        values.put("departments", page.getContent());

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
