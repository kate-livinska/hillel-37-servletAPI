package ua.hillel.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.annotation.WebServlet;
import ua.hillel.model.Order;
import ua.hillel.service.ProductsInMemoryService;
import ua.hillel.service.ProductsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@WebServlet("/orders")
public class ProductServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "application/json";
    private final ProductsService productsService;
    private ObjectMapper mapper;


    public ProductServlet() {
        this.productsService = new ProductsInMemoryService();
        this.mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getQueryString() != null) {
            String queryString = req.getQueryString();
            String[] parts = queryString.split("=");
            if (parts.length == 2) {
                String productId = parts[1];

                Optional<Order> optionalOrderById = productsService.getOrderById(Long.parseLong(productId));
                if (optionalOrderById.isPresent()) {
                    Order order = optionalOrderById.get();

                    try {
                        String orderJson = mapper.writeValueAsString(order);
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.setContentType(CONTENT_TYPE);
                        resp.getWriter().write(orderJson);
                        resp.getWriter().flush();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = mapper.readValue(req.getReader(), Order.class);
        productsService.addOrder(order);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = mapper.readValue(req.getReader(), Order.class);
        productsService.updateOrder(order);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        productsService.deleteOrder(Long.parseLong(req.getParameter("id")));
    }
}
