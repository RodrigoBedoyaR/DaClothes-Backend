package nl.fontys.s3.daclothes.domain;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetProductsResponse {
    private List<Product> products;
}
