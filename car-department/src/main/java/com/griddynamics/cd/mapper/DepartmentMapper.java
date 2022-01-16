package com.griddynamics.cd.mapper;

import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.model.Department;
import com.griddynamics.cd.model.create.CreateDepartmentRequest;
import com.griddynamics.cd.model.update.UpdateDepartmentRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    Department toDepartmentModel(DepartmentEntity entity);

    DepartmentEntity toDepartmentEntity(CreateDepartmentRequest request);

    @Mapping(target = "name", source = "request.name",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", source = "request.email",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", source = "request.description",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "departmentType", source = "request.departmentType",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DepartmentEntity toDepartmentEntity(UpdateDepartmentRequest request, @MappingTarget DepartmentEntity entity);
}
