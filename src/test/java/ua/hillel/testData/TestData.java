package ua.hillel.testData;

import ua.hillel.model.Order;
import ua.hillel.model.Product;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestData {
    public static final List<Product> PRODUCTS1 = new ArrayList<>() {
        {
            add(new Product(101L, "Laptop", 1000.50));
            add(new Product(102L, "Mouse", 25.99));
            add(new Product(103L, "Keyboard", 50.25));
        }
    };
    public static final List<Product> PRODUCTS2 = new ArrayList<>() {
        {
            add(new Product(104L, "Smartphone", 700.99));
            add(new Product(105L, "Charger", 15.75));
        }
    };
    public static final Order ORDER1 = new Order(1L, LocalDate.parse("2025-02-15"), 150.75, PRODUCTS1);
    public static final Order ORDER2 = new Order(2L, LocalDate.parse("2025-02-16"), 85.99, PRODUCTS2);
    public static final Order NEW_ORDER = new Order(4L, LocalDate.parse("2025-02-18"), 150.75, PRODUCTS2);
    public static final Order NULL_ID_ORDER = new Order(null, LocalDate.now(), 0.0, TestData.PRODUCTS1);

    public static final Long ID = 1L;
    public static final Long ID_NONEXISTENT = 100L;
    public static final String QUERY_STR = "id=1";
    public static final String QUERY_NONEXISTENT = "id=100";
    public static final String QUERY_INVALID = "id=abc";

    public static final String ORDER_JSON = "{\n  \"id\" : 1,\n  \"date\" : \"2025-02-15\",\n  \"cost\" : 150.75,\n  \"products\" : [ {\n    \"id\" : 101,\n    \"name\" : \"Laptop\",\n    \"cost\" : 1000.5\n  }, {\n    \"id\" : 102,\n    \"name\" : \"Mouse\",\n    \"cost\" : 25.99\n  }, {\n    \"id\" : 103,\n    \"name\" : \"Keyboard\",\n    \"cost\" : 50.25\n  } ]\n}";
    public static final String INVALID_JSON = "{ \"id\" : 1, \"date\" : , \"products\" : [] }";
}
