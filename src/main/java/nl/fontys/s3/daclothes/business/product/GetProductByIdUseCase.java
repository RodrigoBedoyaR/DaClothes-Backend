package nl.fontys.s3.daclothes.business.product;

import nl.fontys.s3.daclothes.domain.Product;

import java.util.Optional;

public interface GetProductByIdUseCase {
    Optional<Product> getProductById(long id);
}
