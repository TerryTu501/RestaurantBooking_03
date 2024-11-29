package com.booking.restaurant.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.booking.restaurant.controller.api.RestaurantApiController;
import com.booking.restaurant.model.Restaurant;
import com.booking.restaurant.model.RestaurantPhotos;
import com.booking.restaurant.model.User;
import com.booking.restaurant.repository.PhotoRepository;
import com.booking.restaurant.repository.RestaurantRepository;

@Service
public class RestaurantPhotosService {

	private static final Logger logger = LoggerFactory.getLogger(RestaurantPhotosService.class);

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * 保存照片並更新封面
     */
    public RestaurantPhotos savePhoto(Integer restaurantId, MultipartFile photo, User uploadedBy) throws IOException {
        // 確保照片文件有效
        if (photo == null || photo.isEmpty()) {
            throw new IOException("照片文件為空");
        }

        // 查詢餐廳對象
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IOException("餐廳不存在，ID: " + restaurantId));

        // 獲取現有封面照片
        RestaurantPhotos existingCoverPhoto = photoRepository
                .findFirstByRestaurantAndPhotoTypeAndIsActive(restaurantId, "cover", true);

        if (existingCoverPhoto != null) {
            // 更新現有照片
            existingCoverPhoto.setFileName(photo.getOriginalFilename());
            existingCoverPhoto.setFileExtension(getFileExtension(photo.getOriginalFilename()));
            existingCoverPhoto.setMimeType(photo.getContentType());
            existingCoverPhoto.setFileSize((int) photo.getSize());
            existingCoverPhoto.setPhotoData(photo.getBytes());
            existingCoverPhoto.setUpdatedAt(LocalDateTime.now());

            return photoRepository.save(existingCoverPhoto);
        }

        // 創建新照片
        RestaurantPhotos restaurantPhoto = new RestaurantPhotos();
        restaurantPhoto.setRestaurant(restaurant);
        restaurantPhoto.setPhotoType("cover"); // 默認為封面照片
        restaurantPhoto.setFileName(photo.getOriginalFilename());
        restaurantPhoto.setFileExtension(getFileExtension(photo.getOriginalFilename()));
        restaurantPhoto.setMimeType(photo.getContentType());
        restaurantPhoto.setFileSize((int) photo.getSize());
        restaurantPhoto.setPhotoData(photo.getBytes());
        restaurantPhoto.setUploadBy(uploadedBy);
        restaurantPhoto.setIsActive(true);
        restaurantPhoto.setCreatedAt(LocalDateTime.now());
        restaurantPhoto.setUpdatedAt(LocalDateTime.now());
        
        // **記錄新增的照片名稱**
        logger.info("照片保存成功，照片名稱: {}", photo.getOriginalFilename());

        return photoRepository.save(restaurantPhoto);
    }

    
    // 取得單一餐廳照片
    public RestaurantPhotos findCoverPhotoByRestaurantId(Integer restaurantId) {
        return photoRepository.findFirstByRestaurantAndPhotoTypeAndIsActive(restaurantId, "cover", true);
    }
    
    /**
     * 生成照片的公開訪問 URL
     */
    public String generatePhotoUrl(Integer photoId) {
        // 假設 API 的 URL 模板
        return String.format("https://your-domain.com/api/photos/image/%d", photoId);
    }


    
//    public RestaurantPhotos savePhoto(Restaurant restaurant, MultipartFile photo, User uploadedBy) throws IOException {
//        RestaurantPhotos restaurantPhoto = new RestaurantPhotos();
//
//        // 設置基本屬性
//        restaurantPhoto.setRestaurant(restaurant);
//        restaurantPhoto.setPhotoType("cover"); // 你可以根據需求設置不同的 photoType
//        restaurantPhoto.setFileName(photo.getOriginalFilename());
//        restaurantPhoto.setFileExtension(getFileExtension(photo.getOriginalFilename()));
//        restaurantPhoto.setMimeType(photo.getContentType());
//        restaurantPhoto.setFileSize((int) photo.getSize());
//        restaurantPhoto.setPhotoData(photo.getBytes());
//        restaurantPhoto.setUploadBy(uploadedBy);
//
//        // 設置其他屬性
//        restaurantPhoto.setIsActive(true);
//        restaurantPhoto.setCreatedAt(LocalDateTime.now());
//        restaurantPhoto.setUpdatedAt(LocalDateTime.now());
//
//        return photoRepository.save(restaurantPhoto);
//    }

    // Helper method to extract file extension
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }
}
