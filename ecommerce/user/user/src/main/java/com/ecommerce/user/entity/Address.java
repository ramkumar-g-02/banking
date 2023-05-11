package com.ecommerce.user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "streetName", column = @Column(name = "street")),
        @AttributeOverride(name = "city", column = @Column(name = "city")),
        @AttributeOverride(name = "pinCode", column = @Column(name = "pin_code")),
        @AttributeOverride(name = "buildingNumber", column = @Column(name = "building_number")),
})
@Data
public class Address {
    private Integer buildingNumber;
    private String streetName;
    private String city;
    private Long pinCode;
    @Transient
    private String address;

    public String getAddress() {
        return buildingNumber + ", " + streetName + ", " + city + " - " + pinCode;
    }
}
