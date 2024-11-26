package com.booking.restaurant.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;

//import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnorePropertie。;。

@Entity
@Table(name = "RestaurantPhotos", schema = "dbo")  // 明確指定 schema 和表名
public class RestaurantPhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer photoId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    
    @Column(name = "photo_type", nullable = false, length = 20)
    private String photoType;
    
    @Lob
    @JsonIgnore // 忽略這個屬性，避免返回大量二進制數據
    @Column(name = "photo_data", nullable = false)
    private byte[] photoData;
    
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "file_extension", nullable = false, length = 10)
    private String fileExtension;
    
    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;
    
    @Column(name = "file_size", nullable = false)
    private Integer fileSize;
    
    @Column(name = "photo_description")
    private String photoDescription;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_by")
    private User uploadBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public RestaurantPhotos() {
    }
    
    @Override
	public String toString() {
		return "RestaurantPhotos [photoId=" + photoId + ", restaurant=" + restaurant + ", photoType=" + photoType
				+ ", photoData=" + Arrays.toString(photoData) + ", fileName=" + fileName + ", fileExtension="
				+ fileExtension + ", mimeType=" + mimeType + ", fileSize=" + fileSize + ", photoDescription="
				+ photoDescription + ", displayOrder=" + displayOrder + ", isActive=" + isActive + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}



	// Getters and Setters
    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public User getUploadBy() {
        return uploadBy;
    }

    public void setUploadBy(User uploadBy) {
        this.uploadBy = uploadBy;
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

    // JPA Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}