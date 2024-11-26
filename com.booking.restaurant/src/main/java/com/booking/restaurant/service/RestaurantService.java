package com.booking.restaurant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.booking.restaurant.dto.ApiResponse;
import com.booking.restaurant.dto.MenuDTO;
import com.booking.restaurant.dto.MenuItemDTO;
import com.booking.restaurant.dto.RestaurantMenuDTO;
import com.booking.restaurant.dto.TableDTO;
import com.booking.restaurant.model.Menu;
import com.booking.restaurant.model.MenuItem;
import com.booking.restaurant.model.Restaurant;
import com.booking.restaurant.model.RestaurantTable;
import com.booking.restaurant.repository.MenuItemRepository;
import com.booking.restaurant.repository.MenuRepository;
import com.booking.restaurant.repository.RestaurantRepository;
import com.booking.restaurant.repository.RestaurantTableRepository;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private MenuRepository menuRepository;    
    
    @Autowired
    private MenuItemRepository menuItemRepository;
    
    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

//  顯示餐廳資料
    public List<Restaurant> getRestaurantsByOwner(Integer ownerId) {
        // 使用您在 RestaurantRepository 中定義的方法 findByOwnerId
        return restaurantRepository.findByOwner_UserId(ownerId);
    }
     
//  保存新增的餐廳資料
    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }
    
//  編輯前，取得餐廳資料
    public Restaurant getRestaurantById(Integer id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        return optionalRestaurant.orElse(null);
    }    
   
//  更新餐廳資料
    public boolean updateRestaurant(Restaurant restaurant) {
        if (restaurantRepository.existsById(restaurant.getRestaurantId())) {
            restaurantRepository.save(restaurant); // 保存更新後資料
            return true;
        }
        return false;
    }
       
 // 刪除餐廳資料
    public boolean deleteRestaurantById(Integer id) {
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
//---------- 菜單相關    
    
//  顯示菜單
    public List<RestaurantMenuDTO> getMenusByRestaurantId(Integer restaurantId) {
        // 查找菜單
        List<Menu> menus = menuRepository.findByRestaurantRestaurantId(restaurantId);
        System.out.println("Menus size: " + menus.size()); // Debugging: 檢查菜單大小
        System.out.println("Menus: " + menus); // Debugging: 檢查菜單內容
        
        // 構建 RestaurantMenuDTO 列表
        return menus.stream().map(menu -> {
            RestaurantMenuDTO menuDTO = new RestaurantMenuDTO();
            menuDTO.setMenuId(menu.getMenuId());
            menuDTO.setName(menu.getName());
            menuDTO.setDescription(menu.getDescription());
            menuDTO.setIsActive(menu.getIsActive());

            // 構建菜品項目的 DTO 列表
            List<MenuItemDTO> itemDTOs = menu.getMenuItem().stream().map(item -> {
                MenuItemDTO itemDTO = new MenuItemDTO();
                itemDTO.setItemId(item.getItemId());
                itemDTO.setName(item.getName());
                itemDTO.setDescription(item.getDescription());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setCategory(item.getCategory());
                itemDTO.setIsAvailable(item.getIsAvailable());
                return itemDTO;
            }).collect(Collectors.toList());

            menuDTO.setItems(itemDTOs);
            return menuDTO;
        }).collect(Collectors.toList());
    }

//  新增菜單   
    @Transactional
    public Menu addMenu(Integer restaurantId, MenuDTO menuDTO) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "餐廳未找到"));

        Menu newMenu = new Menu();
        newMenu.setRestaurant(restaurant);
        newMenu.setRestaurantId(restaurant.getRestaurantId()); // 設置 restaurantId
        newMenu.setName(menuDTO.getName());
        newMenu.setDescription(menuDTO.getDescription());
        newMenu.setIsActive(menuDTO.getIsActive());

        List<MenuItem> menuItems = menuDTO.getItems().stream().map(itemDTO -> {
            MenuItem menuItem = new MenuItem();
            menuItem.setMenu(newMenu);
            menuItem.setName(itemDTO.getName());
            menuItem.setDescription(itemDTO.getDescription());
            menuItem.setPrice(itemDTO.getPrice());
            menuItem.setCategory(itemDTO.getCategory());
            menuItem.setIsAvailable(itemDTO.getIsAvailable());
            return menuItem;
        }).collect(Collectors.toList());

        newMenu.setMenuItem(menuItems);

        return menuRepository.save(newMenu);
    }

//	編輯前，取得菜單資料
    public MenuDTO getMenuById(Integer restaurantId, Integer menuId) {
        // 查找餐廳
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "餐廳未找到"));

        // 查找菜單
        Menu menu = menuRepository.findById(menuId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "菜單未找到"));

        // 確保菜單屬於該餐廳
        if (!menu.getRestaurant().getRestaurantId().equals(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該菜單不屬於此餐廳");
        }

        // 將菜單轉換為 DTO
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setMenuId(menu.getMenuId());
        menuDTO.setName(menu.getName());
        menuDTO.setDescription(menu.getDescription());
        menuDTO.setIsActive(menu.getIsActive());

        // 將菜單項目轉換為 DTO
        List<MenuItemDTO> itemDTOs = menu.getMenuItem().stream().map(item -> {
            MenuItemDTO itemDTO = new MenuItemDTO();
            itemDTO.setItemId(item.getItemId());
            itemDTO.setName(item.getName());
            itemDTO.setDescription(item.getDescription());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setCategory(item.getCategory());
            itemDTO.setIsAvailable(item.getIsAvailable());
            return itemDTO;
        }).collect(Collectors.toList());

        menuDTO.setItems(itemDTOs);

        return menuDTO;
    }
    
//  編輯菜單
    @Transactional
    public Menu updateMenu(Integer restaurantId, Integer menuId, MenuDTO menuDTO) {
        // 查找餐廳和菜單
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "餐廳未找到"));
        
        Menu existingMenu = menuRepository.findById(menuId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "菜單未找到"));

        // 更新菜單屬性
        existingMenu.setName(menuDTO.getName());
        existingMenu.setDescription(menuDTO.getDescription());
        existingMenu.setIsActive(menuDTO.getIsActive());

        // 更新菜單項目
        List<MenuItem> updatedItems = new ArrayList<>();
        for (MenuItemDTO itemDTO : menuDTO.getItems()) {
            MenuItem menuItem;
            
            if (itemDTO.getItemId() != null) {
                // 更新現有的菜單項目
                menuItem = menuItemRepository.findById(itemDTO.getItemId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "菜單項目未找到"));
            } else {
                // 新增新的菜單項目
                menuItem = new MenuItem();
                menuItem.setMenu(existingMenu);
            }
            
            // 設置菜單項目屬性
            menuItem.setName(itemDTO.getName());
            menuItem.setDescription(itemDTO.getDescription());
            menuItem.setPrice(itemDTO.getPrice());
            menuItem.setCategory(itemDTO.getCategory());
            menuItem.setIsAvailable(itemDTO.getIsAvailable());
            updatedItems.add(menuItem);
        }

        // 清除並重新設置菜單項目
        existingMenu.getMenuItem().clear();
        existingMenu.getMenuItem().addAll(updatedItems);

        // 保存並返回菜單
        return menuRepository.save(existingMenu);
    }

//	刪除整個菜單(1)  
    @Transactional
    public boolean deleteMenu(Integer restaurantId, Integer menuId) {
        if (menuRepository.existsByMenuIdAndRestaurantRestaurantId(menuId, restaurantId)) {
            menuRepository.deleteById(menuId);
            return true;
        }
        return false;
    }
    
//  刪除單一菜品(2)
    @Transactional
    public boolean deleteMenuItem(Integer restaurantId, Integer menuId, Integer itemId) {
        if (menuItemRepository.existsByItemIdAndMenuMenuIdAndMenuRestaurantRestaurantId(itemId, menuId, restaurantId)) {
            menuItemRepository.deleteById(itemId);
            return true;
        }
        return false;
    }
    
//---------- 桌位相關    
    
//  顯示桌位    
    public List<RestaurantTable> getTablesByRestaurantId(Integer restaurantId) {
        // 驗證餐廳 ID 是否存在
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到餐廳");
        }

        // 獲取餐廳的所有桌位資料
        return restaurantTableRepository.findByRestaurantRestaurantId(restaurantId);
    }
    
//  新增桌位  
    @Transactional
    public RestaurantTable addTable(Integer restaurantId, String tableNumber, Integer capacity, String status) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "餐廳未找到"));

        RestaurantTable table = new RestaurantTable();
        table.setRestaurant(restaurant);
        table.setTableNumber(tableNumber);
        table.setCapacity(capacity);
        table.setStatus(RestaurantTable.TableStatus.valueOf(status.toUpperCase()));

        return restaurantTableRepository.save(table);
    }

// 	編輯前，取得桌位資料
    public TableDTO getTableById(Integer restaurantId, Integer tableId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到餐廳"));

        Optional<RestaurantTable> optionalTable = restaurantTableRepository.findById(tableId);
        if (optionalTable.isPresent() && optionalTable.get().getRestaurant().equals(restaurant)) {
            RestaurantTable table = optionalTable.get();
            TableDTO tableDTO = new TableDTO();
            tableDTO.setTableId(table.getTableId());
            tableDTO.setTableNumber(table.getTableNumber());
            tableDTO.setCapacity(table.getCapacity());
            tableDTO.setStatus(table.getStatus().name());
            return tableDTO;
        }
        return null;
    }
    
//  編輯桌位
    @Transactional
    public boolean updateTable(Integer restaurantId, Integer tableId, TableDTO tableDTO) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到餐廳"));

        Optional<RestaurantTable> optionalTable = restaurantTableRepository.findById(tableId);
        if (optionalTable.isPresent() && optionalTable.get().getRestaurant().equals(restaurant)) {
            RestaurantTable table = optionalTable.get();
            table.setTableNumber(tableDTO.getTableNumber());
            table.setCapacity(tableDTO.getCapacity());
            table.setStatus(RestaurantTable.TableStatus.valueOf(tableDTO.getStatus().toUpperCase()));
            restaurantTableRepository.save(table);
                    
            tableDTO.setTableId(table.getTableId());// 確保 tableId 被設置
            return true;
        }
        return false;
    }

//	刪除桌位	
    @Transactional
    public boolean deleteTable(Integer restaurantId, Integer tableId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到餐廳"));

        return restaurantTableRepository.findById(tableId).map(table -> {
            if (table.getRestaurant().equals(restaurant)) {
                restaurantTableRepository.delete(table);
                return true;
            }
            return false;
        }).orElse(false);
    }


}
