package com.booking.restaurant.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.booking.restaurant.model.Restaurant;
import com.booking.restaurant.model.RestaurantPhotos;
import com.booking.restaurant.repository.PhotoRepository;

@Service
public class PhotoService {

	private static final Logger logger = LoggerFactory.getLogger(PhotoService.class);
	private final PhotoRepository photoRepository;


	public PhotoService(PhotoRepository photoRepository) {
		this.photoRepository = photoRepository;
	}

	// 根據餐廳ID獲取照片
	public List<RestaurantPhotos> getRestaurantPhotos(Integer restaurantId) {
		logger.info("Fetching photos for restaurant: {}", restaurantId);
		return photoRepository.findByRestaurantId(restaurantId);
	}

	// 根據類型獲取照片
	public List<RestaurantPhotos> getRestaurantPhotosByType(Integer restaurantId, String photoType) {
		logger.info("Fetching photos for restaurant: {} and type: {}", restaurantId, photoType);
		return photoRepository.findByRestaurantIdAndPhotoType(restaurantId, photoType);
	}

	// 獲取單張照片
	public Optional<RestaurantPhotos> getPhoto(Integer id) {
		return photoRepository.findById(id);
	}

	// 保存照片
	@Transactional
	public RestaurantPhotos savePhoto(MultipartFile file, Restaurant restaurant, String photoType, String description)
			throws IOException {
		logger.info("開始保存照片 - 餐廳ID: {}", restaurant.getRestaurantId());

// 1. 讀取檔案內容
		byte[] photoData = file.getBytes();
		logger.info("成功讀取檔案內容 - 大小: {} bytes", photoData.length);

// 2. 準備照片實體
		RestaurantPhotos photo = new RestaurantPhotos();

		try {
// 設置基本資訊
			photo.setRestaurant(restaurant);
			photo.setUploadBy(restaurant.getOwner());
			photo.setPhotoType(photoType);
			photo.setPhotoDescription(description);

// 設置檔案資訊
			photo.setPhotoData(photoData);
			photo.setFileName(file.getOriginalFilename());
			photo.setFileExtension(getFileExtension(file.getOriginalFilename()));
			photo.setMimeType(file.getContentType());
			photo.setFileSize((int) file.getSize());

// 設置排序和時間戳
			photo.setDisplayOrder(getNextDisplayOrder(restaurant.getRestaurantId()));
			photo.setCreatedAt(LocalDateTime.now());
			photo.setUpdatedAt(LocalDateTime.now());

			logger.info("照片實體準備完成 - 準備保存到資料庫");

// 3. 保存到資料庫
			RestaurantPhotos savedPhoto = photoRepository.save(photo);
			logger.info("照片成功保存到資料庫 - 新ID: {}", savedPhoto.getPhotoId());

			return savedPhoto;

		} catch (Exception e) {
			logger.error("保存照片時發生錯誤", e);
			throw new RuntimeException("保存照片失敗: " + e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private void validateFile(MultipartFile file) {
		if (file == null) {
			throw new IllegalArgumentException("檔案不能為空");
		}
		if (file.isEmpty()) {
			throw new IllegalArgumentException("檔案內容不能為空");
		}
		if (file.getSize() > 5 * 1024 * 1024) { // 5MB
			throw new IllegalArgumentException("檔案大小不能超過5MB");
		}
		String contentType = file.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) {
			throw new IllegalArgumentException("只能上傳圖片檔案");
		}
	}

	// 刪除照片 - 直接刪除
	@Transactional
    public void deletePhoto(Integer id) {
        logger.info("正在刪除照片，ID: {}", id);
        try {
            // 檢查照片是否存在
            if (!photoRepository.existsById(id)) {
                throw new RuntimeException("找不到ID為 " + id + " 的照片");
            }
            photoRepository.deleteById(id);
            logger.info("照片刪除成功，ID: {}", id);
        } catch (Exception e) {
            logger.error("刪除照片時發生錯誤，ID: {}", id, e);
            throw new RuntimeException("刪除照片失敗: " + e.getMessage());
        }
    }


	// 獲取下一個顯示順序號
	  private Integer getNextDisplayOrder(Integer restaurantId) {
	        Integer maxOrder = photoRepository.findMaxDisplayOrder(restaurantId);
	        return (maxOrder != null) ? maxOrder + 1 : 1;
	    }
	    
	    private String getFileExtension(String filename) {
	        if (filename == null || filename.lastIndexOf(".") == -1) {
	            return "";
	        }
	        return filename.substring(filename.lastIndexOf(".") + 1);
	    }

}