package com.booking.restaurant.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
//import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.booking.restaurant.dto.RestaurantPhotosDTO;
import com.booking.restaurant.model.Restaurant;
import com.booking.restaurant.model.RestaurantPhotos;
import com.booking.restaurant.service.PhotoService;
import com.booking.restaurant.service.RestaurantService;

/**
 * 照片相關API控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/api/photos")
public class PhotoApiController {
    private static final Logger logger = LoggerFactory.getLogger(PhotoApiController.class);
    
    private final PhotoService photoService;
    private final RestaurantService restaurantService;
    
    public PhotoApiController(PhotoService photoService, RestaurantService restaurantService) {
        this.photoService = photoService;
        this.restaurantService = restaurantService;
    }
      
    /**
     * 獲取餐廳的所有照片
     */
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<List<RestaurantPhotosDTO>> getRestaurantPhotos(@PathVariable Integer restaurantId) {
        try {
            List<RestaurantPhotos> photos = photoService.getRestaurantPhotos(restaurantId);

            // 如果無照片，返回空的列表
            if (photos.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }

            // 將照片列表轉換為 DTO
            List<RestaurantPhotosDTO> photosDTO = photos.stream()
                .map(photo -> new RestaurantPhotosDTO(
                    photo.getPhotoId(),
                    photo.getPhotoType(),
                    photo.getFileName(),
                    photo.getFileExtension(),
                    photo.getMimeType(),
                    photo.getFileSize(),
                    photo.getPhotoDescription(),
                    photo.getDisplayOrder(),
                    photo.getIsActive(),
                    photo.getCreatedAt(),
                    photo.getUpdatedAt()
                ))
                .collect(Collectors.toList());
            // **記錄獲取的照片數量**
            logger.info("獲取照片成功，照片數量: {}", photosDTO.size());

            return ResponseEntity.ok(photosDTO);
        } catch (Exception e) {
            logger.error("獲取餐廳照片失敗，餐廳ID: {}", restaurantId, e);
            return ResponseEntity.status(500).body(null);
        }
    }
      
    
    /**     
     * 上傳照片
     */
    @PostMapping("/restaurants/{restaurantId}")
    public ResponseEntity<?> uploadPhoto(
            @PathVariable Integer restaurantId,
            @RequestParam("file") MultipartFile file,
            @RequestParam String photoType,
            @RequestParam(required = false) String description) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
            RestaurantPhotos photo = photoService.savePhoto(file, restaurant, photoType, description);
            // 返回成功消息或返回一個新的 DTO
            return ResponseEntity.ok(new RestaurantPhotosDTO(
                photo.getPhotoId(),
                photo.getPhotoType(),
                photo.getFileName(),
                photo.getFileExtension(),
                photo.getMimeType(),
                photo.getFileSize(),
                photo.getPhotoDescription(),
                photo.getDisplayOrder(),
                photo.getIsActive(),
                photo.getCreatedAt(),
                photo.getUpdatedAt()
            ));
        } catch (Exception e) {
            logger.error("Error uploading photo", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
       
    
    
    
    
    /**
     * 獲取指定ID的圖片(含圖片類型&描述)
     */
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        logger.info("請求圖片，ID: {}", id);
        try {
            Optional<RestaurantPhotos> photoOpt = photoService.getPhoto(id);
            
            if (photoOpt.isPresent()) {
                RestaurantPhotos photo = photoOpt.get();
                byte[] imageData = photo.getPhotoData();
                
                if (imageData != null && imageData.length > 0) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType(photo.getMimeType()));
                    headers.setContentLength(imageData.length);
                    
                    return ResponseEntity.ok()
                        .headers(headers)
                        .body(imageData);
                }
            }
            
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error reading image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 刪除照片
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePhoto(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            photoService.deletePhoto(id);
            response.put("status", "success");
            response.put("message", "照片刪除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    /**
     * 獲取指定ID的圖片(含圖片類型&描述)
     */
//    @GetMapping("/image/{id}")
//    public ResponseEntity<ImageResponseDTO> getImage(@PathVariable Integer id) {
//        logger.info("請求圖片，ID: {}", id);
//        try {
//            Optional<RestaurantPhotos> photoOpt = photoService.getPhoto(id);
//
//            if (photoOpt.isPresent()) {
//                RestaurantPhotos photo = photoOpt.get();
//                byte[] imageData = photo.getPhotoData();
//
//                if (imageData != null && imageData.length > 0) {
//                    // 使用 DTO 返回圖片資訊
//                    ImageResponseDTO imageResponse = new ImageResponseDTO(
//                        photo.getPhotoType(),
//                        photo.getPhotoDescription(),
//                        imageData
//                    );
//
//                    HttpHeaders headers = new HttpHeaders();
//                    headers.setContentType(MediaType.APPLICATION_JSON); // 設置返回類型
//
//                    return ResponseEntity.ok()
//                        .headers(headers)
//                        .body(imageResponse);
//                }
//            }
//
//            // 使用 DTO 返回錯誤訊息
//            ImageResponseDTO errorResponse = new ImageResponseDTO(
//                null,
//                "找不到此 ID: " + id,
//                null
//            );
//
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//        } catch (Exception e) {
//            logger.error("Error reading image", e);
//            // 使用你的 DTO 返回錯誤訊息
//            ImageResponseDTO errorResponse = new ImageResponseDTO(
//                null,
//                "伺服器內部錯誤: " + e.getMessage(),
//                null
//            );
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//    }
}
