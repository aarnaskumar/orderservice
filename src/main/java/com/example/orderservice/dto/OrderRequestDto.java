package com.example.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderRequestDto {

    @Schema(description = "ID of the user placing the order", example = "123")
    @NotNull(message = "User ID is required")
    private Long userId;

    @Schema(description = "ID of the product being ordered", example = "456")
    @NotNull(message = "Product ID is required")
    private Long productId;

    @Schema(description = "Quantity of the product", example = "2", minimum = "1")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

    @Schema(description = "Price per unit of the product", example = "99.99", minimum = "0")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private double price;

    // Getters and Setters
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
}
