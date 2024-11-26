package com.booking.restaurant.controller.api;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.booking.restaurant.dto.ApiResponse;
import com.booking.restaurant.dto.MenuDTO;
import com.booking.restaurant.dto.MenuItemDTO;
import com.booking.restaurant.dto.RestaurantMenuDTO;
import com.booking.restaurant.dto.TableDTO;
import com.booking.restaurant.model.Menu;
import com.booking.restaurant.model.Restaurant;
import com.booking.restaurant.model.RestaurantTable;
import com.booking.restaurant.model.User;
import com.booking.restaurant.repository.RestaurantRepository;
import com.booking.restaurant.repository.RestaurantTableRepository;
import com.booking.restaurant.service.RestaurantPhotosService;
import com.booking.restaurant.service.RestaurantService;
import com.booking.restaurant.validation.JsonWebTokenUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/api/restaurant")
public class RestaurantApiController {

//	 private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);
	@Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;
		
	@Autowired
    private RestaurantRepository restaurantRepository;
    
	@Autowired
    private RestaurantService restaurantService;
    
    @Autowired
    private RestaurantPhotosService restaurantPhotosService;
    
    @Autowired
    private RestaurantTableRepository restaurantTableRepository;
    
//  顯示所有餐廳
    @GetMapping("/manage")
    public ResponseEntity<?> showRestaurants(@RequestHeader("Authorization") String authorizationHeader) {
    	System.out.println("Authorization Header: " + authorizationHeader); // 確認日誌
    	try {
            // 驗證和解析 JWT Token
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Error: 未登入或無效的使用者 ID");
            }
            String token = authorizationHeader.substring(7);
            String subject = jsonWebTokenUtility.validateToken(token);

            if (subject == null || subject.isEmpty()) {
                return ResponseEntity.status(401).body("Error: 無效的 Token");
            }

            // 從 Token 主體中提取 ownerId
            JSONObject userJson = new JSONObject(subject);
            Integer ownerId = userJson.getInt("userId");

            // 獲取餐廳資料
            List<Restaurant> restaurants = restaurantService.getRestaurantsByOwner(ownerId);

            if (restaurants.isEmpty()) {
                return ResponseEntity.ok().body(new ApiResponse("error", "目前無相關餐廳資料", null));
            }

            return ResponseEntity.ok().body(new ApiResponse("success", "餐廳資料獲取成功", restaurants));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Error: 請求處理失敗，請稍後再試");
        }
    }

//  新增餐廳-post
    @PostMapping("/add")
    public ResponseEntity<?> addRestaurant(
            @RequestPart("restaurant") String restaurantJson,  // 接收餐廳 JSON 字符串
            @RequestPart("photos") MultipartFile photos,       // 接收照片文件
            @RequestHeader("Authorization") String authorizationHeader) {

        try {
            // 驗證和解析 JWT Token
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(new ApiResponse("error", "未登入或無效的使用者 ID", null));
            }
            String token = authorizationHeader.substring(7);
            String subject = jsonWebTokenUtility.validateToken(token);

            if (subject == null || subject.isEmpty()) {
                return ResponseEntity.status(401).body(new ApiResponse("error", "無效的 Token", null));
            }

            // 從 Token 主體中提取 ownerId
            JSONObject userJson = new JSONObject(subject);
            Integer ownerId = userJson.getInt("userId");

            // 解析餐廳 JSON 字符串
            Restaurant restaurant = new ObjectMapper().readValue(restaurantJson, Restaurant.class);

            // 設置擁有者
            User owner = new User();
            owner.setUserId(ownerId);
            restaurant.setOwner(owner);

            // 保存餐廳資料
            Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurant);

            // 保存照片
            restaurantPhotosService.savePhoto(savedRestaurant, photos, owner);

            return ResponseEntity.ok(new ApiResponse("success", "餐廳資料新增成功", savedRestaurant));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(new ApiResponse("error", "照片保存失敗", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(new ApiResponse("error", "新增餐廳失敗，請重試", null));
        }
    }

// 	編輯前，先取得餐廳資料，渲染於前端表單    
    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Integer id, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 驗證和解析 JWT Token
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(new ApiResponse("error", "未登入或無效的使用者 ID", null));
            }
            String token = authorizationHeader.substring(7);
            String subject = jsonWebTokenUtility.validateToken(token);

            if (subject == null || subject.isEmpty()) {
                return ResponseEntity.status(401).body(new ApiResponse("error", "無效的 Token", null));
            }

            Restaurant restaurant = restaurantService.getRestaurantById(id);
            if (restaurant != null) {
                return ResponseEntity.ok().body(new ApiResponse("success", "獲取餐廳資料成功", restaurant));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse("error", "找不到該餐廳", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("error", "獲取餐廳資料失敗，請重試", null));
        }
    }  
        
//  編輯餐廳資料
    @PutMapping("/edit")
    public ResponseEntity<?> editRestaurant(
            @RequestPart("restaurant") String restaurantJson,  // 接收餐廳 JSON 字符串
            @RequestPart("photos") MultipartFile photos,       // 接收新的照片文件
            @RequestHeader("Authorization") String authorizationHeader) {

        try {
            // 驗證和解析 JWT Token
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(new ApiResponse("error", "未登入或無效的使用者 ID", null));
            }
            String token = authorizationHeader.substring(7);
            String subject = jsonWebTokenUtility.validateToken(token);

            if (subject == null || subject.isEmpty()) {
                return ResponseEntity.status(401).body(new ApiResponse("error", "無效的 Token", null));
            }

            // 從 Token 主體中提取 ownerId
            JSONObject userJson = new JSONObject(subject);
            Integer ownerId = userJson.getInt("userId");

            // 解析餐廳 JSON 字符串
            Restaurant restaurant = new ObjectMapper().readValue(restaurantJson, Restaurant.class);
            User owner = new User();
            owner.setUserId(ownerId);
            restaurant.setOwner(owner);

            // 檢查餐廳是否存在並更新
            boolean isUpdated = restaurantService.updateRestaurant(restaurant);
            if (!isUpdated) {
                return ResponseEntity.status(404).body(new ApiResponse("error", "找不到該餐廳", null));
            }

            // 更新照片
            restaurantPhotosService.savePhoto(restaurant, photos, owner);

            return ResponseEntity.ok().body(new ApiResponse("success", "餐廳資料更新成功", restaurant));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(new ApiResponse("error", "照片保存失敗", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse("error", "更新失敗，請重試", null));
        }
    }
    
//  刪除餐廳資料
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Integer id, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 驗證和解析 JWT Token
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(new ApiResponse("error", "未登入或無效的使用者 ID"));
            }
            String token = authorizationHeader.substring(7);
            String subject = jsonWebTokenUtility.validateToken(token);

            if (subject == null || subject.isEmpty()) {
                return ResponseEntity.status(401).body(new ApiResponse("error", "無效的 Token"));
            }

            // 檢查餐廳是否存在並刪除
            boolean isDeleted = restaurantService.deleteRestaurantById(id);
            if (isDeleted) {
                return ResponseEntity.ok().body(new ApiResponse("success", "餐廳資料刪除成功", id, isDeleted));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse("error", "找不到該餐廳"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("error", "刪除失敗，請重試"));
        }
    }

//  -----與菜單有關
	
//  顯示菜單
    @GetMapping("/{restaurantId}/menus")
    public ResponseEntity<ApiResponse> getRestaurantMenus(@PathVariable Integer restaurantId) {
        try {
            // 從服務中獲取餐廳的菜單
            List<RestaurantMenuDTO> menus = restaurantService.getMenusByRestaurantId(restaurantId);

            // 檢查是否獲取到菜單
            if (menus == null || menus.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse("error", "找不到餐廳菜單資料", 0, null));
            }

            // 返回成功響應
            return ResponseEntity.ok(new ApiResponse("success", "餐廳菜單資料獲取成功", menus.size(), menus));
        } catch (Exception e) {
            // 捕獲並打印異常詳細信息，便於調試
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse("error", "伺服器錯誤", 0, null));
        }
    }

//	新增菜單
    @PostMapping("/{restaurantId}/menus/add")
    public ResponseEntity<ApiResponse> addMenuToRestaurant(
        @PathVariable Integer restaurantId, 
        @RequestBody MenuDTO menuDTO) {
        
        try {
            Menu newMenu = restaurantService.addMenu(restaurantId, menuDTO);
            
            // 創建新的 MenuDTO 來返回數據
            MenuDTO responseMenuDTO = new MenuDTO();
            responseMenuDTO.setMenuId(newMenu.getMenuId());
            responseMenuDTO.setName(newMenu.getName());
            responseMenuDTO.setDescription(newMenu.getDescription());
            responseMenuDTO.setIsActive(newMenu.getIsActive());

            // 設置菜單項目
            List<MenuItemDTO> itemDTOs = newMenu.getMenuItem().stream().map(item -> {
                MenuItemDTO itemDTO = new MenuItemDTO();
                itemDTO.setItemId(item.getItemId());
                itemDTO.setName(item.getName());
                itemDTO.setDescription(item.getDescription());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setCategory(item.getCategory());
                itemDTO.setIsAvailable(item.getIsAvailable());
                return itemDTO;
            }).collect(Collectors.toList());
            
            responseMenuDTO.setItems(itemDTOs);

            return ResponseEntity.ok(new ApiResponse("success", "菜單新增成功", 1, responseMenuDTO));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ApiResponse("error", e.getReason(), 0, null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse("error", "新增菜單失敗", 0, null));
        }
    }
  
//	編輯前，先取得餐廳資料，渲染於前端表單     
    @GetMapping("/{restaurantId}/menus/{menuId}")
    public ResponseEntity<ApiResponse> getMenuById(
        @PathVariable Integer restaurantId,
        @PathVariable Integer menuId) {

        try {
            // 從服務層獲取菜單資料
            MenuDTO menuDTO = restaurantService.getMenuById(restaurantId, menuId);
            
            if (menuDTO == null) {
                return ResponseEntity.status(404).body(new ApiResponse("error", "菜單未找到", 0, null));
            }
            
            return ResponseEntity.ok(new ApiResponse("success", "菜單資料獲取成功", 1, menuDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse("error", "獲取菜單資料失敗", 0, null));
        }
    }

//	編輯菜單
    @PutMapping("/{restaurantId}/menus/{menuId}/edit")
    public ResponseEntity<ApiResponse> updateMenu(
        @PathVariable Integer restaurantId,
        @PathVariable Integer menuId,
        @RequestBody MenuDTO menuDTO) {

        try {
            Menu updatedMenu = restaurantService.updateMenu(restaurantId, menuId, menuDTO);

            // 建立返回的 MenuDTO
            MenuDTO responseMenuDTO = new MenuDTO();
            responseMenuDTO.setMenuId(updatedMenu.getMenuId());
            responseMenuDTO.setName(updatedMenu.getName());
            responseMenuDTO.setDescription(updatedMenu.getDescription());
            responseMenuDTO.setIsActive(updatedMenu.getIsActive());

            // 設置菜單項目
            List<MenuItemDTO> itemDTOs = updatedMenu.getMenuItem().stream().map(item -> {
                MenuItemDTO itemDTO = new MenuItemDTO();
                itemDTO.setItemId(item.getItemId());
                itemDTO.setName(item.getName());
                itemDTO.setDescription(item.getDescription());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setCategory(item.getCategory());
                itemDTO.setIsAvailable(item.getIsAvailable());
                return itemDTO;
            }).collect(Collectors.toList());

            responseMenuDTO.setItems(itemDTOs);

            return ResponseEntity.ok(new ApiResponse("success", "菜單更新成功", 1, responseMenuDTO));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ApiResponse("error", e.getReason(), 0, null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse("error", "菜單更新失敗", 0, null));
        }
    }

//	刪除整個菜單(1)    
    @DeleteMapping("/{restaurantId}/menus/{menuId}/delete")
    public ResponseEntity<ApiResponse> deleteMenu(
        @PathVariable Integer restaurantId,
        @PathVariable Integer menuId) {
        try {
            boolean isDeleted = restaurantService.deleteMenu(restaurantId, menuId);
            if (isDeleted) {
                return ResponseEntity.ok(new ApiResponse("success", "菜單刪除成功", 1, null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "菜單未找到", 0, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("error", "刪除菜單失敗", 0, null));
        }
    }
    
//	刪除單一菜品(2)      
    @DeleteMapping("/{restaurantId}/menus/{menuId}/items/{itemId}/delete")
    public ResponseEntity<ApiResponse> deleteMenuItem(
        @PathVariable Integer restaurantId,
        @PathVariable Integer menuId,
        @PathVariable Integer itemId) {
        try {
            boolean isDeleted = restaurantService.deleteMenuItem(restaurantId, menuId, itemId);
            if (isDeleted) {
                return ResponseEntity.ok(new ApiResponse("success", "菜品刪除成功", 1, null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "菜品未找到", 0, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("error", "刪除菜品失敗", 0, null));
        }
    }


//  -----桌位相關
	
//  顯示桌位   
    @GetMapping("/{restaurantId}/tables")
    public ResponseEntity<?> getTablesByRestaurantId(@PathVariable Integer restaurantId) {
        try {
            // 使用 Service 層獲取桌位資料
            List<RestaurantTable> tables = restaurantService.getTablesByRestaurantId(restaurantId);

            // 將實體轉換為 DTO
            List<TableDTO> tableDTOs = tables.stream().map(table -> {
                TableDTO dto = new TableDTO();
                dto.setTableId(table.getTableId());
                dto.setTableNumber(table.getTableNumber());
                dto.setCapacity(table.getCapacity());
                dto.setStatus(table.getStatus().toString());
                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok().body(tableDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\": \"error\", \"message\": \"伺服器內部錯誤\", \"count\": 0, \"data\": null}");
        }
    }

//	新增桌位
    @PostMapping("/{restaurantId}/tables/add")
    public ResponseEntity<ApiResponse> addTable(@PathVariable Integer restaurantId, @RequestBody TableDTO tableDTO) {
        // 驗證餐廳 ID 是否存在
        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);
        if (!restaurantOpt.isPresent()) {
            return ResponseEntity.status(404).body(new ApiResponse("error", "找不到指定 ID 的餐廳"));
        }

        // 創建並保存新的桌位
        Restaurant restaurant = restaurantOpt.get();
        RestaurantTable newTable = new RestaurantTable();
        newTable.setRestaurant(restaurant);
        newTable.setTableNumber(tableDTO.getTableNumber());
        newTable.setCapacity(tableDTO.getCapacity());
        newTable.setStatus(RestaurantTable.TableStatus.valueOf(tableDTO.getStatus().toUpperCase()));

        RestaurantTable savedTable = restaurantTableRepository.save(newTable);

        // 構建返回的 TableDTO
        TableDTO responseDTO = new TableDTO();
        responseDTO.setTableId(savedTable.getTableId());
        responseDTO.setTableNumber(savedTable.getTableNumber());
        responseDTO.setCapacity(savedTable.getCapacity());
        responseDTO.setStatus(savedTable.getStatus().toString());

        // 返回成功訊息，並封裝成 ApiResponse
        ApiResponse response = new ApiResponse("success", "桌位新增成功", 1, responseDTO);
        return ResponseEntity.ok(response);
    }

 // 編輯前，先取得餐廳資料
    @GetMapping("/{restaurantId}/tables/{tableId}")
    public ResponseEntity<ApiResponse> getTableById(
            @PathVariable Integer restaurantId,
            @PathVariable Integer tableId) {

        TableDTO tableDTO = restaurantService.getTableById(restaurantId, tableId);
        if (tableDTO != null) {
            return ResponseEntity.ok(new ApiResponse("success", "桌位資料獲取成功", 1, tableDTO));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse("error", "找不到指定的餐廳或桌位", 0, null));
        }
    }
    
//	編輯桌位
    @PutMapping("/{restaurantId}/tables/{tableId}/edit")
    public ResponseEntity<ApiResponse> updateTable(
            @PathVariable Integer restaurantId,
            @PathVariable Integer tableId,
            @RequestBody TableDTO tableDTO) {

        boolean isUpdated = restaurantService.updateTable(restaurantId, tableId, tableDTO);

        if (isUpdated) {
        	tableDTO.setTableId(tableId);
            return ResponseEntity.ok(new ApiResponse("success", "桌位更新成功", 1, tableDTO));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse("error", "找不到指定的餐廳或桌位", 0, null));
        }
    }
    
// 	刪除桌位
    @DeleteMapping("/{restaurantId}/tables/{tableId}/delete")
    public ResponseEntity<ApiResponse> deleteTable(
            @PathVariable Integer restaurantId,
            @PathVariable Integer tableId) {
        try {
            boolean isDeleted = restaurantService.deleteTable(restaurantId, tableId);
            if (isDeleted) {
                return ResponseEntity.ok(new ApiResponse("success", "桌位刪除成功", 1, null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("error", "找不到指定的餐廳或桌位", 0, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "伺服器錯誤，刪除失敗", 0, null));
        }
    }
    
}
