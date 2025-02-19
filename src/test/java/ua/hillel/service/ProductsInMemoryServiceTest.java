package ua.hillel.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.hillel.model.Order;
import ua.hillel.testData.TestData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductsInMemoryServiceTest {
    private ProductsService productsService;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        productsService = new ProductsInMemoryService();
        orders = new ArrayList<>();
        orders.add(TestData.ORDER1);
        orders.add(TestData.ORDER2);
        productsService.setOrders(orders);
    }

    @Test
    void testAddOrder_OK() {
        boolean result = productsService.addOrder(TestData.NEW_ORDER);

        assertTrue(result);
        assertEquals(3, orders.size());
        assertTrue(orders.contains(TestData.NEW_ORDER));
    }

    @Test
    void testAddOrder_existingOrder() {
        Order testOrder = new Order(1L, LocalDate.now(), 0.0, TestData.PRODUCTS1);
        boolean result = productsService.addOrder(testOrder);

        assertFalse(result);
        assertEquals(2, orders.size());
        assertFalse(orders.contains(testOrder));
    }

    @Test
    void testAddOrder_null() {
        boolean result = productsService.addOrder(null);

        assertFalse(result);
        assertEquals(2, orders.size());
    }

    @Test
    void testAddOrder_withNullId() {
        boolean result = productsService.addOrder(TestData.NULL_ID_ORDER);

        assertFalse(result);
        assertEquals(2, orders.size());
    }

    @Test
    void getOrderById_OK() {
        Optional<Order> result = productsService.getOrderById(TestData.ID);

        assertNotNull(result);
        assertEquals(TestData.ID, result.get().getId());
        assertEquals(TestData.PRODUCTS1, result.get().getProducts());
    }

    @Test
    void getOrderById_nonExistentOrder() {
        Optional<Order> result = productsService.getOrderById(TestData.ID_NONEXISTENT);

        assertFalse(result.isPresent());
    }

    @Test
    void updateOrder_OK() {
        Order testOrder = new Order(1L, LocalDate.now(), 0.0, TestData.PRODUCTS2);
        boolean result = productsService.updateOrder(testOrder);

        assertTrue(result);
        assertEquals(2, orders.size());
        assertTrue(orders.contains(testOrder));
        assertEquals(TestData.PRODUCTS2, orders.get(0).getProducts());
    }

    @Test
    void updateOrder_noSuchOrder() {
        boolean result = productsService.updateOrder(TestData.NEW_ORDER);
        assertFalse(result);
        assertEquals(2, orders.size());
        assertFalse(orders.contains(TestData.NEW_ORDER));
    }

    @Test
    void updateOrder_OrderIsNull() {
        boolean result = productsService.updateOrder(null);
        assertFalse(result);
    }

    @Test
    void updateOrder_withNullId() {
        boolean result = productsService.updateOrder(TestData.NULL_ID_ORDER);
        assertFalse(result);
    }

    @Test
    void deleteOrder_OK() {
        boolean result = productsService.deleteOrder(TestData.ID);
        assertTrue(result);
        assertEquals(1, orders.size());
        assertFalse(orders.contains(TestData.ORDER1));
    }

    @Test
    void deleteOrder_nonExistentOrder() {
        boolean result = productsService.deleteOrder(TestData.ID_NONEXISTENT);
        assertFalse(result);
    }

    @Test
    void deleteOrder_withNullId() {
        boolean result = productsService.deleteOrder(null);
        assertFalse(result);
    }
}