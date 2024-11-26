package com.booking.restaurant.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.booking.restaurant.model.Restaurant;
import com.booking.restaurant.model.RestaurantPhotos;
import com.booking.restaurant.model.User;
import com.booking.restaurant.repository.PhotoRepository;

@Service
public class RestaurantPhotosService {

    @Autowired
    private PhotoRepository photoRepository;

    public RestaurantPhotos savePhoto(Restaurant restaurant, MultipartFile photo, User uploadedBy) throws IOException {
        RestaurantPhotos restaurantPhoto = new RestaurantPhotos();

        // 設置基本屬性
        restaurantPhoto.setRestaurant(restaurant);
        restaurantPhoto.setPhotoType("cover"); // 你可以根據需求設置不同的 photoType
        restaurantPhoto.setFileName(photo.getOriginalFilename());
        restaurantPhoto.setFileExtension(getFileExtension(photo.getOriginalFilename()));
        restaurantPhoto.setMimeType(photo.getContentType());
        restaurantPhoto.setFileSize((int) photo.getSize());
        restaurantPhoto.setPhotoData(photo.getBytes());
        restaurantPhoto.setUploadBy(uploadedBy);

        // 設置其他屬性
        restaurantPhoto.setIsActive(true);
        restaurantPhoto.setCreatedAt(LocalDateTime.now());
        restaurantPhoto.setUpdatedAt(LocalDateTime.now());

        return photoRepository.save(restaurantPhoto);
    }

    // Helper method to extract file extension
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }
}
