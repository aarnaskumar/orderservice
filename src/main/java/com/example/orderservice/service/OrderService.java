package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.dto.OrderResponseDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Create a new order
     */
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {
        // Basic validation is handled by @Valid in controller, but we can add more business logic here
        Order order = new Order(
                requestDto.getUserId(),
                requestDto.getProductId(),
                requestDto.getQuantity(),
                requestDto.getPrice(),
                OrderStatus.CREATED
        );
        Order savedOrder = orderRepository.save(order);
        return mapToResponseDto(savedOrder);
    }

    /**
     * Get order by ID
     */
    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return mapToResponseDto(order);
    }

    /**
     * Get paginated list of orders
     */
    public Page<OrderResponseDto> getOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(this::mapToResponseDto);
    }

    /**
     * Helper method to map Order entity to OrderResponseDto
     */
    private OrderResponseDto mapToResponseDto(Order order) {
        return new OrderResponseDto(
                order.getOrderId(),
                order.getUserId(),
                order.getProductId(),
                order.getQuantity(),
                order.getPrice(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
