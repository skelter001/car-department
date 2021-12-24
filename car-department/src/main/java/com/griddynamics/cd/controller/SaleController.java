package com.griddynamics.cd.controller;

import com.griddynamics.cd.model.Sale;
import com.griddynamics.cd.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/departments")
@AllArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @GetMapping("/{departmentId}/sales")
    @Operation(
            summary = "Get all sales for particular department by its id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public List<Sale> getSalesByDepartmentId(@PathVariable Long departmentId) {
        return saleService.getSalesByDepartmentId(departmentId);
    }

    @PostMapping("/{departmentId}/sales")
    @Operation(
            summary = "Save sale model",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public void saveSale(@PathVariable Long departmentId,
                         @RequestBody Sale sale) {
        saleService.saveSale(sale);
    }

    @DeleteMapping("/sales/{/saleId}")
    @Operation(
            summary = "Delete sale by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public void deleteSale(@PathVariable Long saleId) {
        saleService.deleteSaleById(saleId);
    }
}
