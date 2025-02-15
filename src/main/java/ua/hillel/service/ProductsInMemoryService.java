package ua.hillel.service;

import ua.hillel.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsInMemoryService implements ProductsService {
    private List<Order> orders = new ArrayList<Order>();

    @Override
    public void addOrder(Order order) {
        orders.add(order);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        if (orders.isEmpty()) {
            return Optional.empty();
        } else {
            return orders.stream()
                    .filter(order -> order.getId().equals(id))
                    .findFirst();
        }
    }

    @Override
    public void updateOrder(Order order) {
        if (orders.contains(order)) {
            orders.stream()
                    .filter(o -> o.getId().equals(order.getId()))
                    .findFirst()
                    .ifPresent(o -> {
                        o.setDate(order.getDate());
                        o.setProducts(order.getProducts());
                        o.setCost(order.getCost());
                    });
        } else {
            orders.add(order);
        }
    }

    @Override
    public void deleteOrder(Long id) {
        orders.removeIf(o -> o.getId().equals(id));
    }
}
