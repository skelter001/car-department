package com.griddynamics.cd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@Builder
@AllArgsConstructor
public class Sale {
    @NonFinal
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    Integer totalPrice;
    String info;
    Long departmentId;

    @JsonCreator
    public Sale(@JsonProperty("totalPrice") Integer totalPrice,
                @JsonProperty("info") String info,
                @JsonProperty(value = "departmentId", required = true) Long departmentId) {
        this.totalPrice = totalPrice;
        this.info = info;
        this.departmentId = departmentId;
    }
}
