package com.booking.restaurant.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users" , schema = "dbo")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;
    
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;
    
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    
//    @Column(name = "area", length = 50)
//    private String area;
       
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Column(name = "user_type", nullable = false, length = 20)
    private String userType;  // 'customer', 'restaurant_owner', 'admin'
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 與餐廳的關聯（針對餐廳老闆）
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Restaurant> ownedRestaurants = new ArrayList<>();
    
    // 與餐廳照片的關聯（作為上傳者）
    @OneToMany(mappedBy = "uploadBy", cascade = CascadeType.ALL)
    private List<RestaurantPhotos> uploadedPhotos = new ArrayList<>();
    
    // 與通知的關聯 (做為發送者或接收者)
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> sentNotifications = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> receivedNotifications = new ArrayList<>();
           
    // Constructor
    public User() {
    }
    
    // Helper methods for managing relationships
    public void addRestaurant(Restaurant restaurant) {
        ownedRestaurants.add(restaurant);
        restaurant.setOwner(this);
    }
    
    public void removeRestaurant(Restaurant restaurant) {
        ownedRestaurants.remove(restaurant);
        restaurant.setOwner(null);
    }
    
    public void addUploadedPhoto(RestaurantPhotos photo) {
        uploadedPhotos.add(photo);
        photo.setUploadBy(this);
    }
    
    public void removeUploadedPhoto(RestaurantPhotos photo) {
        uploadedPhotos.remove(photo);
        photo.setUploadBy(null);
    }
    
    // 管理通知的助手方法
    public void addSentNotification(Notification notification) {
        sentNotifications.add(notification);
        notification.setSender(this);
    }

    public void addReceivedNotification(Notification notification) {
        receivedNotifications.add(notification);
        notification.setReceiver(this);
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
    
    // toString method
    @Override
    public String toString() {
        return "User [userId=" + userId + 
               ", username=" + username + 
               ", email=" + email + 
               ", phoneNumber=" + phoneNumber + 
               ", userType=" + userType + "]";
    }
    
    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer ownerId) {
        this.userId = ownerId;
    }

    public String getUsername() {
        return username;
    }

//    public String getArea() {
//		return area;
//	}
//
//	public void setArea(String area) {
//		this.area = area;
//	}

	public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public List<Restaurant> getOwnedRestaurants() {
        return ownedRestaurants;
    }

    public void setOwnedRestaurants(List<Restaurant> ownedRestaurants) {
        this.ownedRestaurants = ownedRestaurants;
    }

    public List<RestaurantPhotos> getUploadedPhotos() {
        return uploadedPhotos;
    }

    public void setUploadedPhotos(List<RestaurantPhotos> uploadedPhotos) {
        this.uploadedPhotos = uploadedPhotos;
    }
}