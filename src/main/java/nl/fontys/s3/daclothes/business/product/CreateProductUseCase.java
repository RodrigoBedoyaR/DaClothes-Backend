package nl.fontys.s3.daclothes.business.product;

import nl.fontys.s3.daclothes.domain.CreateProductRequest;
import nl.fontys.s3.daclothes.domain.CreateProductResponse;

public interface CreateProductUseCase {
    CreateProductResponse createProduct( CreateProductRequest request );

}
