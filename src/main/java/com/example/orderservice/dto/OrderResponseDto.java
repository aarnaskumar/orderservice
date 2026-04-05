package com.example.orderservice.dto;

import com.example.orderservice.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public class OrderResponseDto {

    @Schema(description = "Unique identifier of the order", example = "1")
    private Long orderId;

    @Schema(description = "ID of the user who placed the order", example = "123")
    private Long userId;

    @Schema(description = "ID of the ordered product", example = "456")
    private Long productId;

    @Schema(description = "Quantity of the product ordered", example = "2")
    private int quantity;

    @Schema(description = "Price per unit of the product", example = "99.99")
    private double price;

    @Schema(description = "Current status of the order", example = "CREATED")
    private OrderStatus status;

    @Schema(description = "Timestamp when the order was created", example = "2023-10-01T12:00:00")
    private LocalDateTime createdAt;

    // Constructor
    public OrderResponseDto(Long orderId, Long userId, Long productId, int quantity, double price, OrderStatus status, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
