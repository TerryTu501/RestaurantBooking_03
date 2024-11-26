package com.booking.restaurant.dto;

import java.util.List;

public class MenuDTO {
    private Integer menuId;
    private String name;
    private String description;
    private Boolean isActive;
    private List<MenuItemDTO> items;

    // 自定義構造函數，用於初始化 DTO
    public MenuDTO(Integer menuId, String name, String description, Boolean isActive, List<MenuItemDTO> items) {
        this.menuId = menuId;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.items = items;
    }    
    
    // Constructors
    public MenuDTO() {}

    // Getters and Setters
    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<MenuItemDTO> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDTO> items) {
        this.items = items;
    }
}

