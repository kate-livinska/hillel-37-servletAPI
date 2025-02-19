package ua.hillel.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import ua.hillel.model.Order;
import ua.hillel.service.ProductsService;
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


    public ProductServlet(ProductsService productsService) {
        this.productsService = productsService;
        this.mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .registerModule(new JavaTimeModule())
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Long id = parseIdFromInput(req);
        if (id < 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            Optional<Order> optionalOrderById = productsService.getOrderById(id);
            if (optionalOrderById.isPresent()) {
                Order order = optionalOrderById.get();
                try {
                    String orderJson = mapper.writeValueAsString(order);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.setContentType(CONTENT_TYPE);
                    resp.getWriter().write(orderJson);
                    resp.getWriter().flush();
                } catch (IOException e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Order order = mapper.readValue(req.getReader(), Order.class);
            if (productsService.addOrder(order)) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Order order = mapper.readValue(req.getReader(), Order.class);
            if (productsService.updateOrder(order)) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        Long id = parseIdFromInput(req);
        if (id < 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            if (productsService.deleteOrder(id)) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    private Long parseIdFromInput(HttpServletRequest req) {
        Long id = -1L;
        if (req.getQueryString() == null) {
            return id;
        }
        try {
            String queryString = req.getQueryString();
            String[] parts = queryString.split("=");
            if (parts.length == 2) {
                String productId = parts[1];
                id = Long.parseLong(productId);
            }
        } catch (NumberFormatException e) {
            return id;
        }
        return id;
    }
}
