package com.griddynamics.cd.service;

import com.griddynamics.cd.entity.SaleEntity;
import com.griddynamics.cd.model.Sale;
import com.griddynamics.cd.repository.SaleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;

    public List<Sale> getSalesByDepartmentId(Long departmentId) {
        return saleRepository.findAllSalesByDepartmentId(departmentId)
                .stream()
                .map(this::mapEntityToModel)
                .collect(Collectors.toList());
    }

    public void saveSale(Sale sale) {
        saleRepository.save(mapModelToEntity(sale));
    }

    public void deleteSaleById(Long id) {
        saleRepository.deleteById(id);
    }

    public SaleEntity mapModelToEntity(Sale sale) {
        return SaleEntity.builder()
                .id(sale.getId())
                .totalPrice(sale.getTotalPrice())
                .info(sale.getInfo())
                .departmentId(sale.getDepartmentId())
                .build();
    }

    public Sale mapEntityToModel(SaleEntity entity) {
        return Sale.builder()
                .id(entity.getId())
                .totalPrice(entity.getTotalPrice())
                .info(entity.getInfo())
                .departmentId(entity.getDepartmentId())
                .build();
    }
}
