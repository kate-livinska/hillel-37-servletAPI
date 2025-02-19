package ua.hillel.service;

import ua.hillel.model.Order;

import java.util.List;
import java.util.Optional;

public interface ProductsService {
    boolean addOrder(Order order);
    Optional<Order> getOrderById(Long id);
    boolean updateOrder(Order order);
    boolean deleteOrder(Long id);

    void setOrders(List<Order> ordersList);
}
