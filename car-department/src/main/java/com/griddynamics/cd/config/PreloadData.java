package com.griddynamics.cd.config;

import com.griddynamics.cd.entity.CarEntity;
import com.griddynamics.cd.entity.ColorEntity;
import com.griddynamics.cd.entity.DepartmentEntity;
import com.griddynamics.cd.entity.EmployeeEntity;
import com.griddynamics.cd.entity.SaleEntity;
import com.griddynamics.cd.repository.CarRepository;
import com.griddynamics.cd.repository.ColorRepository;
import com.griddynamics.cd.repository.DepartmentRepository;
import com.griddynamics.cd.repository.EmployeeRepository;
import com.griddynamics.cd.repository.SaleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@Configuration
@AllArgsConstructor
@Profile("dev")
public class PreloadData implements CommandLineRunner {

    private final CarRepository carRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final SaleRepository saleRepository;
    private final ColorRepository colorRepository;

    @Override
    public void run(String... args) {

        departmentRepository.save(
                DepartmentEntity.builder()
                        .name("Department 1")
                        .support("support 1")
                        .email("test@department1.com")
                        .description("Some description of this department")
                        .build()
        );

        colorRepository.save(
                ColorEntity.builder()
                        .id(1L)
                        .colorName("BLACK")
                        .build()
        );

        colorRepository.save(
                ColorEntity.builder()
                        .id(2L)
                        .colorName("GREY")
                        .build()
        );

        saleRepository.save(
                SaleEntity.builder()
                        .id(1L)
                        .totalPrice(123)
                        .info("Some info about sale")
                        .departmentId(1L)
                        .build()
        );

        saleRepository.save(
                SaleEntity.builder()
                        .id(2L)
                        .totalPrice(321)
                        .info("Some info about sale")
                        .departmentId(1L)
                        .build()
        );

        employeeRepository.save(
                EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .birthday(LocalDate.of(1970, 1, 1))
                        .address("46 Myrtle Street\n" +
                                "Red Oak, TX 75154")
                        .phoneNumber("(469) 200-8258")
                        .departmentId(1L)
                        .build()
        );

        employeeRepository.save(
                EmployeeEntity.builder()
                        .firstName("Richard")
                        .lastName("Doe")
                        .birthday(LocalDate.of(1969, 12, 20))
                        .address("5657 Phelps St\n" +
                                "The Colony, Texas(TX), 75056 ")
                        .phoneNumber("(469) 200-8313")
                        .departmentId(1L)
                        .build()
        );

        carRepository.save(
                CarEntity.builder()
                        .id(2L)
                        .manufacturer("manufacturer1")
                        .model("a1")
                        .vinNumber("vinNumber1")
                        .color(ColorEntity.builder().colorName("grey").build())
                        .employeeId(1L)
                        .build()
        );

        carRepository.save(
                CarEntity.builder()
                        .id(3L)
                        .manufacturer("manufacturer1")
                        .model("a2")
                        .vinNumber("vinNumber2")
                        .color(ColorEntity.builder().colorName("black").build())
                        .employeeId(1L)
                        .build()
        );

        carRepository.save(
                CarEntity.builder()
                        .id(4L)
                        .manufacturer("manufacturer1")
                        .model("a2")
                        .vinNumber("vinNumber2")
                        .color(ColorEntity.builder().colorName("red").build())
                        .employeeId(2L)
                        .build()
        );
    }
}
