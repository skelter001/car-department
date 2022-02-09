package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findAllEmployeesByDepartmentId(Long departmentId);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdIsNot(String phoneNumber, Long id);

    @Query(value = "SELECT e FROM EmployeeEntity as e WHERE " +
            "(:firstName is null or e.firstName like %:firstName% )" +
            "and " + "(:lastName is null or e.lastName like %:lastName%) " +
            "and " + "(:birthday is null or e.birthday=:birthday) " +
            "and " + "(:address is null or e.address like %:address%) " +
            "and " + "(:phoneNumber is null or e.phoneNumber like %:phoneNumber%)")
    List<EmployeeEntity> findAllByFilterParams(@Param(value = "firstName") String firstName,
                                               @Param(value = "lastName") String lastName,
                                               @Param(value = "birthday") LocalDate birthday,
                                               @Param(value = "address") String address,
                                               @Param(value = "phoneNumber") String phoneNumber);
}
