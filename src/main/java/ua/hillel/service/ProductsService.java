package ua.hillel.service;

import ua.hillel.model.Order;

import java.util.Optional;

public interface ProductsService {
    void addOrder(Order order);
    Optional<Order> getOrderById(Long id);
    void updateOrder(Order order);
    void deleteOrder(Long id);
}
