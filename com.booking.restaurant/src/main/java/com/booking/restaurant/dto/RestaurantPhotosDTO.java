package com.booking.restaurant.dto;

import java.time.LocalDateTime;

public class RestaurantPhotosDTO {
    private Integer photoId;
    private String photoType;
    private String fileName;
    private String fileExtension;
    private String mimeType;
    private Integer fileSize;
    private String photoDescription;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
        
 // Default Constructor
    public RestaurantPhotosDTO() {}

    // Constructor with all fields
    public RestaurantPhotosDTO(
        Integer photoId,
        String photoType,
        String fileName,
        String fileExtension,
        String mimeType,
        Integer fileSize,
        String photoDescription,
        Integer displayOrder,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.photoId = photoId;
        this.photoType = photoType;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.photoDescription = photoDescription;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType;
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
}
