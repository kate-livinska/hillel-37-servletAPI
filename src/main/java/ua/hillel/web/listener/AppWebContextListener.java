package ua.hillel.web.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import ua.hillel.service.ProductsInMemoryService;
import ua.hillel.service.ProductsService;
import ua.hillel.web.ProductServlet;

//@WebListener
public class AppWebContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ProductsService productsService = new ProductsInMemoryService();

        ServletContext servletContext = sce.getServletContext();

        ProductServlet servlet = new ProductServlet(productsService);

        servletContext.addServlet("productServlet", servlet)
                .addMapping("/orders");
    }
}
