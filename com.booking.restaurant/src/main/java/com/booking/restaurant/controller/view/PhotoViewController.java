//package com.booking.restaurant.controller.view;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.booking.restaurant.model.Restaurant;
//import com.booking.restaurant.model.RestaurantPhotos;
//import com.booking.restaurant.service.PhotoService;
//import com.booking.restaurant.service.RestaurantService;
//
///**
// * 照片管理視圖控制器
// */
//@Controller
//@RequestMapping("/photos")
//public class PhotoViewController {
//    private static final Logger logger = LoggerFactory.getLogger(PhotoViewController.class);
//    
//    private final PhotoService photoService;
//    private final RestaurantService restaurantService;
//    
//    public PhotoViewController(PhotoService photoService, RestaurantService restaurantService) {
//        this.photoService = photoService;
//        this.restaurantService = restaurantService;
//    }
//    
//    /**
//     * 顯示照片管理頁面
//     */
//    @GetMapping("/manage")
//    public String showManagePage(
//            @RequestParam(required = false) Integer restaurantId,
//            @RequestParam(required = false) String photoType,
//            Model model) {
//        
//        // 設置照片類型選項
//        model.addAttribute("photoTypes", List.of(
//            "LOGO", "COVER", "INTERIOR", "FOOD", "MENU", "ENVIRONMENT"
//        ));
//        
//        // 初始化空的照片列表
//        model.addAttribute("photos", new ArrayList<RestaurantPhotos>());
//        
//        // 如果有餐廳ID，則查詢照片
//        if (restaurantId != null) {
//            try {
//                List<RestaurantPhotos> photos;
//                if (photoType != null && !photoType.isEmpty()) {
//                    photos = photoService.getRestaurantPhotosByType(restaurantId, photoType);
//                } else {
//                    photos = photoService.getRestaurantPhotos(restaurantId);
//                }
//                
//                model.addAttribute("photos", photos);
//                model.addAttribute("selectedRestaurantId", restaurantId);
//                model.addAttribute("selectedPhotoType", photoType);
//                
//                logger.info("Found {} photos for restaurant {}", photos.size(), restaurantId);
//                
//            } catch (Exception e) {
//                logger.error("Error fetching photos", e);
//                model.addAttribute("errorMessage", "查詢照片失敗: " + e.getMessage());
//            }
//        }
//        
//        return "photo/manage";
//    }
//    
//    /**
//     * 處理照片上傳
//     */
//    @PostMapping("/upload")
//    public String uploadPhoto(
//            @RequestParam("restaurantId") Integer restaurantId,
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("photoType") String photoType,
//            @RequestParam(required = false) String description,
//            RedirectAttributes redirectAttributes) {
//        
//        logger.info("開始處理上傳請求 - 餐廳ID: {}, 檔案名稱: {}, 類型: {}, 描述: {}", 
//                   restaurantId, file.getOriginalFilename(), photoType, description);
//        
//        try {
//            // 驗證餐廳
//            Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
//            if (restaurant == null) {
//                throw new RuntimeException("餐廳不存在，ID: " + restaurantId);
//            }
//            
//            // 驗證檔案
//            if (file.isEmpty()) {
//                throw new RuntimeException("上傳的檔案為空");
//            }
//            if (file.getSize() > 5 * 1024 * 1024) { // 5MB
//                throw new RuntimeException("檔案大小超過5MB限制");
//            }
//            
//            // 保存照片
//            RestaurantPhotos savedPhoto = photoService.savePhoto(file, restaurant, photoType, description);
//            redirectAttributes.addFlashAttribute("success", 
//                String.format("照片上傳成功 (ID: %d)", savedPhoto.getPhotoId()));
//            
//        } catch (Exception e) {
//            logger.error("照片上傳失敗", e);
//            redirectAttributes.addFlashAttribute("error", "照片上傳失敗: " + e.getMessage());
//        }
//        
//        return "redirect:/photos/manage?restaurantId=" + restaurantId;
//    }
//    
//    /**
//     * 處理照片刪除
//     */
//    @PostMapping("/delete")
//    public String deletePhoto(
//            @RequestParam("photoId") Integer photoId,
//            @RequestParam("restaurantId") Integer restaurantId,
//            RedirectAttributes redirectAttributes) {
//        try {
//            photoService.deletePhoto(photoId);
//            redirectAttributes.addFlashAttribute("successMessage", "照片已成功刪除");
//            logger.info("Successfully deleted photo with ID: {}", photoId);
//        } catch (Exception e) {
//            logger.error("Error deleting photo with ID: {}", photoId, e);
//            redirectAttributes.addFlashAttribute("errorMessage", "刪除照片時發生錯誤: " + e.getMessage());
//        }
//        return "redirect:/photos/manage?restaurantId=" + restaurantId;
//    }
//}