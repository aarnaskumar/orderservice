package com.example.orderservice.config;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner preloadOrders(OrderRepository orderRepository) {
        return args -> {
            if (orderRepository.count() > 0) {
                return; // avoid duplicate loading on restart
            }

            List<Order> sampleOrders = new ArrayList<>();
            OrderStatus[] statuses = {OrderStatus.CREATED, OrderStatus.PAID, OrderStatus.CANCELLED};
            LocalDateTime now = LocalDateTime.now();

            for (int i = 1; i <= 25; i++) {
                long userId = 10 + (i % 7);          // varying user IDs 10..16
                long productId = 100 + (i * 5 % 12); // varied product IDs 100..111
                int quantity = 1 + (i % 5);          // 1..5 units
                double price = 19.99 + ((i * 2) % 15); // 19.99..33.99
                OrderStatus status = statuses[i % statuses.length];
                LocalDateTime createdAt = now.minusMinutes(i * 7);

                Order sampleOrder = new Order(userId, productId, quantity, price, status);
                sampleOrder.setCreatedAt(createdAt);
                sampleOrders.add(sampleOrder);
            }

            orderRepository.saveAll(sampleOrders);
        };
    }
}
