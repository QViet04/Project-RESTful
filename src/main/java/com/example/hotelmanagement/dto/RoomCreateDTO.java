package com.example.hotelmanagement.dto;

import java.math.BigDecimal;

import com.example.hotelmanagement.entity.RoomType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RoomCreateDTO {

    @NotBlank
    private String number;

    @NotNull
    private RoomType type;

    @NotNull
    @Min(0)
    private BigDecimal price;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
