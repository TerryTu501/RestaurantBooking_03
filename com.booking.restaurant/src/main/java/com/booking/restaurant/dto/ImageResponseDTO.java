package com.booking.restaurant.dto;

public class ImageResponseDTO {
//    private String photoType;
//    private String photoDescription;
    private byte[] imageData;

    // Constructor
    public ImageResponseDTO(byte[] imageData) {
//        this.photoType = photoType;
//        this.photoDescription = photoDescription;
        this.imageData = imageData;
    }

    // Getters and Setters
    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
    
//    public String getPhotoType() {
//        return photoType;
//    }
//
//    public void setPhotoType(String photoType) {
//        this.photoType = photoType;
//    }
//
//    public String getPhotoDescription() {
//        return photoDescription;
//    }
//
//    public void setPhotoDescription(String photoDescription) {
//        this.photoDescription = photoDescription;
//    }


}
