package com.booking.restaurant.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.booking.restaurant.dto.TableDTO;
import com.booking.restaurant.model.Restaurant;
import com.booking.restaurant.model.RestaurantTable;
import com.booking.restaurant.repository.RestaurantRepository;
import com.booking.restaurant.repository.RestaurantTableRepository;

@Service
public class RestaurantTableService {

	@Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;
	
	
	public RestaurantTable createTable(RestaurantTable table) {
        return restaurantTableRepository.save(table);
    }

	// 編輯前，渲染表單資料
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

    // 編輯桌位
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
            return true;
        }
        return false;
    }
	
	
}
