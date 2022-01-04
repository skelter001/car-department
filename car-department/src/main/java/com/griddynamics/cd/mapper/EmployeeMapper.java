package com.griddynamics.cd.mapper;

import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.model.create.CreateEmployeeRequest;
import com.griddynamics.cd.model.update.UpdateEmployeeRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {CarMapper.class})
public interface EmployeeMapper {

    Employee toEmployeeModel(EmployeeEntity entity);

    EmployeeEntity toEmployeeEntity(CreateEmployeeRequest request);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "department", source = "department")
    EmployeeEntity toEmployeeEntity(EmployeeEntity entity, DepartmentEntity department);

    @Mapping(target = "firstName", source = "request.firstName",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "lastName", source = "request.lastName",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "birthday", source = "request.birthday",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "address", source = "request.address",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "phoneNumber", source = "request.phoneNumber",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    EmployeeEntity toEmployeeEntity(UpdateEmployeeRequest request, @MappingTarget EmployeeEntity entity);
}
