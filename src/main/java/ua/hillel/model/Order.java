package ua.hillel.model;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class Order {
    private Long id;
    private Date date;
    private double cost;
    private List<Product> products;
}
