package ua.hillel.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mock;
import ua.hillel.service.ProductsInMemoryService;

public class TestParent {
    @Mock
    public static HttpServletRequest req;
    @Mock
    public static HttpServletResponse resp;
    @Mock
    public static ProductsInMemoryService productsService;
}
