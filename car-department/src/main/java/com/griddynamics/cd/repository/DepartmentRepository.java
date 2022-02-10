package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.model.DepartmentType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdIsNot(String email, Long id);

    @Query(value =
            "SELECT * " +
            "FROM department AS d " +
            "WHERE " +
            "(:name IS NULL OR d.name = cast(:name AS TEXT)) " +
            "AND " + "(:email IS NULL OR d.email = cast(:email AS TEXT)) " +
            "AND " + "(:description IS NULL OR d.description = cast(:description AS TEXT)) " +
            "AND " + "(:departmentType IS NULL OR d.department_Type = cast(:#{#departmentType?.name()} AS TEXT)) " +
            " /*#sort*/",
            nativeQuery = true)
    List<DepartmentEntity> findAllByFilterParamsAndSort(@Param(value = "name") String name,
                                                        @Param(value = "email") String email,
                                                        @Param(value = "description") String description,
                                                        @Param(value = "departmentType") DepartmentType departmentType,
                                                        Sort sort);
}
