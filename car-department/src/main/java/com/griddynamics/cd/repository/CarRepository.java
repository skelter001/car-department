package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.CarEntity;
import org.hibernate.jpa.TypedParameterValue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<CarEntity, Long> {

    List<CarEntity> findAllCarsByEmployeeId(Long employeeId);

    @Query(value = """
            SELECT CASE WHEN COUNT(is_c) > 0 THEN TRUE ELSE FALSE END 
            FROM information_schema.columns AS is_c
            WHERE table_name='car' AND column_name=:columnName
            """,
            nativeQuery = true)
    boolean existsByColumnName(@Param("columnName") String columnName);

    @Query(value = """
            SELECT * 
            FROM car AS c 
            WHERE 
            (CAST((:manufacturers) AS VARCHAR) IS NULL OR c.manufacturer = ANY(:manufacturers)) 
            AND (CAST((:models) AS VARCHAR) IS NULL OR c.model = ANY(:models))
            AND (CAST((:vinNumbers) AS VARCHAR) IS NULL OR c.vin_number = ANY(:vinNumbers))
            AND (CAST((:employeeIds) AS VARCHAR) IS NULL OR c.employee_id = ANY(:employeeIds))
            AND (CAST((:colors) AS VARCHAR) IS NULL OR c.color = ANY(:colors))
            /*pageable*/
            """,
            nativeQuery = true)
    List<CarEntity> findAllByFilterParamsAndSortAndPaged(@Param(value = "manufacturers") TypedParameterValue manufacturers,
                                                         @Param(value = "models") TypedParameterValue models,
                                                         @Param(value = "vinNumbers") TypedParameterValue vinNumbers,
                                                         @Param(value = "employeeIds") TypedParameterValue employeeIds,
                                                         @Param(value = "colors") TypedParameterValue colors,
                                                         Pageable pageable);
}
