package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<CarEntity, Long> {

    List<CarEntity> findAllCarsByEmployeeId(Long employeeId);

    @Query(value = "SELECT c FROM CarEntity as c WHERE " +
            "(:manufacturer is null or c.manufacturer like %:manufacturer% )" +
            "and " + "(:model is null or c.model like %:model%) " +
            "and " + "(:vinNumber is null or c.vinNumber like %:vinNumber%) " +
            "and " + "(:color is null or c.color=:color)")
    List<CarEntity> findAllByFilterParams(@Param(value = "manufacturer") String manufacturer,
                                                 @Param(value = "model") String model,
                                                 @Param(value = "vinNumber") String vinNumber,
                                                 @Param(value = "color") Color color);
}
