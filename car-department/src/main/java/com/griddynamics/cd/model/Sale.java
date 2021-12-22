package com.griddynamics.cd.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Sale {
    Long id;
    Integer totalPrice;
    String info;
    Long departmentId;
}
