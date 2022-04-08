package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.EmployeeEntity;
import org.hibernate.jpa.TypedParameterValue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findAllEmployeesByDepartmentId(Long departmentId);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdIsNot(String phoneNumber, Long id);

    @Query(value = """
            SELECT CASE WHEN COUNT(is_c) > 0 THEN TRUE ELSE FALSE END 
            FROM information_schema.columns AS is_c
            WHERE table_name='employee' AND column_name=:columnName
            """,
            nativeQuery = true)
    boolean existsByColumnName(@Param("columnName") String columnName);

    @Query(value = """
            SELECT *
            FROM employee as e 
            WHERE
            (CAST((:firstNames) AS VARCHAR) IS NULL OR e.first_name = ANY(:firstNames))
            AND (CAST((:lastNames) AS VARCHAR) IS NULL OR e.last_name = ANY(:lastNames))
            AND (CAST((:birthdays) AS VARCHAR) IS NULL OR e.birthday = ANY(:birthdays))
            AND (CAST((:addresses) AS VARCHAR) IS NULL OR e.address = ANY(:addresses))
            AND (CAST((:phoneNumbers) AS VARCHAR) IS NULL OR e.phone_number = ANY(:phoneNumbers))
            AND (CAST((:departmentIds) AS VARCHAR) IS NULL OR e.department_id = ANY(:departmentIds))
            """,
            nativeQuery = true)
    List<EmployeeEntity> findAllByFilterParamsAndSortAndPaged(@Param(value = "firstNames") TypedParameterValue firstNames,
                                                              @Param(value = "lastNames") TypedParameterValue lastNames,
                                                              @Param(value = "birthdays") TypedParameterValue birthdays,
                                                              @Param(value = "addresses") TypedParameterValue addresses,
                                                              @Param(value = "phoneNumbers") TypedParameterValue phoneNumbers,
                                                              @Param(value = "departmentIds") TypedParameterValue departmentIds,
                                                              Pageable pageable);
}
