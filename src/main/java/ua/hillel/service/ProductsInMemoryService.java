package ua.hillel.service;

import lombok.Setter;
import ua.hillel.model.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Setter
public class ProductsInMemoryService implements ProductsService {
    private List<Order> orders;

    public ProductsInMemoryService() {
        orders = new ArrayList<>();
    }

    @Override
    public boolean addOrder(Order order) {
        if (order == null || order.getId() == null) {
            return false;
        }
        Optional<Order> foundOrder = orders.stream()
                .filter(o -> o.getId().equals(order.getId()))
                .findFirst();
        if (foundOrder.isPresent()) {
            return false;
        } else {
            return orders.add(order);
        }
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
    public boolean updateOrder(Order order) {
        if (order == null || order.getId() == null) {
            return false;
        }
        Optional<Order> foundOrder = orders.stream()
                .filter(o -> o.getId().equals(order.getId()))
                .findFirst();
        if (foundOrder.isPresent()) {
            foundOrder.get().setDate(order.getDate());
            foundOrder.get().setCost(order.getCost());
            foundOrder.get().setProducts(order.getProducts());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteOrder(Long id) {
        if (orders.isEmpty()) return false;
        else if (id == null) {
            return false;
        }
        return orders.removeIf(o -> o.getId().equals(id));
    }
}
