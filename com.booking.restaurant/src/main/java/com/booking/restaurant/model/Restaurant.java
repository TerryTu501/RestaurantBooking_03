package com.booking.restaurant.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "Restaurants" , schema = "dbo")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Integer restaurantId;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY)
    private Set<RestaurantPhotos> photos = new HashSet<>();
        
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "address", nullable = false, length = 255)
    private String address;
    
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;
    
    @Column(name = "cuisine_type", length = 50)
    private String cuisineType;
    
    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;
    
    @Column(name = "opening_hours", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String openingHours;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    @Column(name = "price_range", nullable = false, length = 20)
    private String priceRange;
    
    @Column(name = "area", nullable = false, length = 100)
    private String area;
    
    @Column(name = "suitable_for", columnDefinition = "NVARCHAR(MAX)")
    private String suitableFor;
    
    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;
    
    @Column(name = "total_reviews")
    private Integer totalReviews;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // Constructor
    public Restaurant() {
    }

   

	@Override
	public String toString() {
		return "Restaurant [restaurantId=" + restaurantId + ", owner=" + owner + ", photos=" + photos + ", name=" + name
				+ ", address=" + address + ", phoneNumber=" + phoneNumber + ", cuisineType=" + cuisineType
				+ ", description=" + description + ", openingHours=" + openingHours + ", status=" + status
				+ ", priceRange=" + priceRange + ", area=" + area + ", suitableFor=" + suitableFor + ", averageRating="
				+ averageRating + ", totalReviews=" + totalReviews + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}



	// Helper methods for managing the bidirectional relationship
    public void addPhoto(RestaurantPhotos photo) {
        photos.add(photo);
        photo.setRestaurant(this);
    }

    public void removePhoto(RestaurantPhotos photo) {
        photos.remove(photo);
        photo.setRestaurant(null);
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

   
    // Getters and Setters
    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSuitableFor() {
        return suitableFor;
    }

    public void setSuitableFor(String suitableFor) {
        this.suitableFor = suitableFor;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<RestaurantPhotos> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<RestaurantPhotos> photos) {
        this.photos = photos;
    }
}