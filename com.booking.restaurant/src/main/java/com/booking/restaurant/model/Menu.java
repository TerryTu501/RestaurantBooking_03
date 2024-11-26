package com.booking.restaurant.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
//import lombok.Getter;
import lombok.NoArgsConstructor;
//import lombok.Setter;


@NoArgsConstructor
@Entity
@Table(name = "Menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id", nullable = false)
    private Integer menuId;

    //顯示錯誤訊息: Caused by: org.hibernate.MappingException: Column 'restaurant_id' is duplicated in mapping for entity 'com.booking.restaurant.model.Menu' (use '@Column(insertable=false, updatable=false)' when mapping multiple properties to the same column)
//    @ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "restaurant_id", nullable = false)
//	private Restaurant restaurant;
    
    @ManyToOne
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    private Restaurant restaurant;
   
    @JsonIgnore // 防止遞迴
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL,  fetch = FetchType.EAGER)
	private List<MenuItem> menuItem;
//    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
//    private List<MenuItem> menuItems = new ArrayList<>();
    
    @Column(name = "restaurant_id", nullable = false)
    private Integer restaurantId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

	@Override
	public String toString() {
		return "Menu [menuId=" + menuId + ", restaurantId=" + restaurantId + ", name=" + name + ", description="
				+ description + ", isActive=" + isActive + "]";
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public List<MenuItem> getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(List<MenuItem> menuItem) {
		this.menuItem = menuItem;
	}

	public Integer getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
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
	
}

