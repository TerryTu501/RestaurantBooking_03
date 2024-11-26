package com.booking.restaurant.dto;

import java.util.Collection;
import java.util.List;

public class ApiResponse {
    private String status;
    private String message;
    private int count; // 新增 count 屬性
    private Object data;
    
    // 構造函數: 僅設置狀態和消息
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.count = 0; // 沒有數據時，設置 count 為 0
        this.data = null; // 沒有數據時，設置 data 為 null
    }

    // 構造函數: 設置狀態、消息、數據筆數和數據
    public ApiResponse(String status, String message, int count, Object data) {
        this.status = status;
        this.message = message;
        this.count = count; // 設置傳入的 count
        this.data = data; // 設置傳入的 data
    }

    // 構造函數: 自動計算筆數（如果 data 是 Collection 類型）
    public ApiResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        if (data instanceof Collection<?>) {
            this.count = ((Collection<?>) data).size(); // 計算 Collection 的大小
        } else {
            this.count = (data != null) ? 1 : 0; // 單個對象的情況設置 count 為 1，否則設置為 0
        }
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
        // 更新資料筆數（如果 data 是 List 類型）
        if (data instanceof List<?>) {
            this.count = ((List<?>) data).size();
        } else {
            this.count = 0;
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
