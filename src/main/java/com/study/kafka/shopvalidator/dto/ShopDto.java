package com.study.kafka.shopvalidator.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ShopDto {
    private String identifier;
    private String status;
    private LocalDate dateShop;
    private List<ShopItemDto> items = new ArrayList<>();
}
