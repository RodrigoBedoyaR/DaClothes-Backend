package nl.fontys.s3.daclothes.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private long id;
    private String name;
    private String description;
    private String size;
    private String category;
    private String brand;
    private String productCondition;
    private double price;
    private User username;
    private boolean available;
}
