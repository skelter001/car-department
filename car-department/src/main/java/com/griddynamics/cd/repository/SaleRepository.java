package com.griddynamics.cd.repository;

import com.griddynamics.cd.entity.SaleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SaleRepository extends CrudRepository<SaleEntity, Long> {
    List<SaleEntity> findAllSalesByDepartmentId(Long departmentId);
}
