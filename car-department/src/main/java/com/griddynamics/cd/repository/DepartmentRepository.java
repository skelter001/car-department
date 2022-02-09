package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.model.DepartmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdIsNot(String email, Long id);

    @Query(value = "SELECT d FROM DepartmentEntity as d WHERE " +
            "(:name is null or d.name like %:name% )" +
            "and " + "(:email is null or d.email like %:email%) " +
            "and " + "(:description is null or d.description like %:description%) " +
            "and " + "(:departmentType is null or d.departmentType=:departmentType)")
    List<DepartmentEntity> findAllByFilterParams(@Param(value = "name") String name,
                                                 @Param(value = "email") String email,
                                                 @Param(value = "description") String description,
                                                 @Param(value = "departmentType") DepartmentType departmentType);
}
