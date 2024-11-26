package com.booking.restaurant.model;

import jakarta.persistence.*;
//import lombok.Getter;
import lombok.NoArgsConstructor;
//import lombok.Setter;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

@NoArgsConstructor
@Entity
@Table(name = "Menu_Items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @JsonIgnore // 防止遞迴
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;
      
//    @ManyToOne
//    @JoinColumn(name = "menu_id", nullable = false)
//    private Menu menu;
    
    // 如果你有一個 menuId 屬性，請使用以下配置
    @Column(name = "menu_id", insertable = false, updatable = false)
    private Integer menuId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

	@Override
	public String toString() {
		return "MenuItem [itemId=" + itemId + ", menuId=" + menuId + ", name=" + name + ", description=" + description
				+ ", price=" + price + ", category=" + category + ", isAvailable=" + isAvailable + "]";
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

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
