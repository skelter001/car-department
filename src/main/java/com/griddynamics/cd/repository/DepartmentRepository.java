package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.DepartmentEntity;
import org.hibernate.jpa.TypedParameterValue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdIsNot(String email, Long id);

    @Query(value = """
            SELECT CASE WHEN COUNT(is_c) > 0 THEN TRUE ELSE FALSE END 
            FROM information_schema.columns AS is_c
            WHERE table_name='department' AND column_name=:columnName
            """,
            nativeQuery = true)
    boolean existsByColumnName(@Param("columnName") String columnName);

    @Query(value = """
            SELECT * 
            FROM department AS d 
            WHERE 
            (CAST((:names) AS VARCHAR) IS NULL OR d.name = ANY(:names)) 
            AND (CAST((:emails) AS VARCHAR) IS NULL OR d.email = ANY(:emails))
            AND (CAST((:descriptions) AS VARCHAR) IS NULL OR d.description = ANY(:descriptions))
            AND (CAST((:departmentTypes) AS VARCHAR) IS NULL OR d.department_type = ANY(:departmentTypes))
            """,
            nativeQuery = true)
    List<DepartmentEntity> findAllByFilterParamsAndSortAndPaged(@Param(value = "names") TypedParameterValue names,
                                                                @Param(value = "emails") TypedParameterValue emails,
                                                                @Param(value = "descriptions") TypedParameterValue descriptions,
                                                                @Param(value = "departmentTypes") TypedParameterValue departmentTypes,
                                                                Pageable pageable);
}
