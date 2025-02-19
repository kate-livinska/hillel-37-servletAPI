package ua.hillel.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.hillel.model.Order;
import ua.hillel.testData.TestData;

import java.io.*;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ProductServletTest extends TestParent {
    @InjectMocks
    private ProductServlet servlet;

    @Mock
    private PrintWriter writer;

    @Mock
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        when(resp.getWriter()).thenReturn(writer);
    }


    @Test
    void testDoGet_validOrderId_orderFoundOK() throws IOException {
        when(req.getQueryString()).thenReturn(TestData.QUERY_STR);
        when(productsService.getOrderById(TestData.ID)).thenReturn(Optional.of(TestData.ORDER1));
        when(mapper.writeValueAsString(TestData.ORDER1)).thenReturn(TestData.ORDER_JSON);

        servlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).setContentType("application/json");
        verify(writer).write(TestData.ORDER_JSON);
    }

    @Test
    void testDoGet_orderNotFound() {
        when(req.getQueryString()).thenReturn(TestData.QUERY_NONEXISTENT);
        when(productsService.getOrderById(TestData.ID_NONEXISTENT)).thenReturn(Optional.empty());

        servlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testDoGet_invalidIdFormat_sendsBadRequestResp() {
        when(req.getQueryString()).thenReturn(TestData.QUERY_INVALID);

        servlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoGet_MissingQueryString() {
        when(req.getQueryString()).thenReturn(null);

        servlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPost_validOrder() throws IOException {
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(TestData.ORDER_JSON)));
        when(mapper.readValue(eq(TestData.ORDER_JSON), eq(Order.class))).thenReturn(TestData.ORDER1);
        when(productsService.addOrder(eq(TestData.ORDER1))).thenReturn(true);

        servlet.doPost(req, resp);

        verify(productsService, times(1)).addOrder(eq(TestData.ORDER1));
        verify(resp).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPost_orderAlreadyExists() throws IOException {
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(TestData.ORDER_JSON)));
        when(mapper.readValue(eq(TestData.ORDER_JSON), eq(Order.class))).thenReturn(TestData.ORDER1);
        when(productsService.addOrder(eq(TestData.ORDER1))).thenReturn(false);

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @Test
    void testDoPost_InvalidJson() throws Exception {
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(TestData.INVALID_JSON)));

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPut_updateOK() throws IOException {
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(TestData.ORDER_JSON)));
        when(mapper.readValue(eq(TestData.ORDER_JSON), eq(Order.class))).thenReturn(TestData.ORDER1);
        when(productsService.updateOrder(eq(TestData.ORDER1))).thenReturn(true);

        servlet.doPut(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPut_orderNotFound() throws IOException {
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(TestData.ORDER_JSON)));
        when(mapper.readValue(eq(TestData.ORDER_JSON), eq(Order.class))).thenReturn(TestData.ORDER1);
        when(productsService.updateOrder(eq(TestData.ORDER1))).thenReturn(false);

        servlet.doPut(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testDoPut_invalidIdJson() throws IOException {
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(TestData.INVALID_JSON)));

        servlet.doPut(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoDelete_OK() {
        when(req.getQueryString()).thenReturn(TestData.QUERY_STR);
        when(productsService.deleteOrder(TestData.ID)).thenReturn(true);

        servlet.doDelete(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoDelete_orderNotFound() {
        when(req.getQueryString()).thenReturn(TestData.QUERY_NONEXISTENT);
        when(productsService.deleteOrder(TestData.ID_NONEXISTENT)).thenReturn(false);

        servlet.doDelete(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testDoDelete_invalidIdFormat() {
        when(req.getQueryString()).thenReturn(TestData.QUERY_INVALID);

        servlet.doDelete(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}