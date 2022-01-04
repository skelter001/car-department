package com.griddynamics.cd.mapper;

import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.model.Department;
import com.griddynamics.cd.model.create.CreateDepartmentRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {EmployeeMapper.class})
public interface DepartmentMapper {

    Department toDepartmentModel(DepartmentEntity entity);

    DepartmentEntity toDepartmentEntity(CreateDepartmentRequest request);

    DepartmentEntity toDepartmentEntity(Department department);
}
