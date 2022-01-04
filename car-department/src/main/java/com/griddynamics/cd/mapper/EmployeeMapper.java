package com.griddynamics.cd.mapper;

import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.model.Employee;
import com.griddynamics.cd.model.EmployeeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {CarMapper.class})
public interface EmployeeMapper {

    Employee toEmployeeModel(EmployeeEntity entity);

    EmployeeEntity toEmployeeEntity(EmployeeRequest request);

    EmployeeEntity toEmployeeEntity(Employee employee);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "department", source = "department")
    EmployeeEntity toEmployeeEntity(EmployeeEntity entity, DepartmentEntity department);
}
