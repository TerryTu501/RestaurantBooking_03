package com.booking.restaurant.dto;

import java.math.BigDecimal;

public class MenuItemDTO {
    private Integer itemId;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Boolean isAvailable;

    // 自定義構造函數，用於初始化 DTO
    public MenuItemDTO(Integer itemId, String name, String description, BigDecimal price, String category, Boolean isAvailable) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = isAvailable;
    }
      
    // Constructors
    public MenuItemDTO() {}

    // Getters and Setters
    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
