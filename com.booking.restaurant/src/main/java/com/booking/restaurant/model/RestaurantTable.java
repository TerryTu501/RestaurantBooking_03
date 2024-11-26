package com.booking.restaurant.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Tables", schema = "dbo")
public class RestaurantTable {
    
    public enum TableStatus {
        AVAILABLE,    // 可用
        OCCUPIED,     // 已占用
        RESERVED,     // 已預訂
        MAINTENANCE   // 維護中
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id")
    private Integer tableId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    
    // 與 Reservation 的關聯
    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private Set<Reservation> reservations = new HashSet<>();
    
    @Column(name = "table_number", nullable = false)
    private String tableNumber;
    
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TableStatus status;
       
    // Constructor
    public RestaurantTable() {
        this.status = TableStatus.AVAILABLE;  // 設置默認狀態
    }
    
    // Helper methods for managing relationships
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setTable(this);
    }
    
    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.setTable(null);
    }
    
    // Getters and Setters
    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    // toString method
    @Override
    public String toString() {
        return "RestaurantTable{" +
               "tableId=" + tableId +
               ", restaurant=" + (restaurant != null ? restaurant.getRestaurantId() : null) +
               ", tableNumber=" + tableNumber +
               ", capacity=" + capacity +
               ", status=" + status +
               '}';
    }
}