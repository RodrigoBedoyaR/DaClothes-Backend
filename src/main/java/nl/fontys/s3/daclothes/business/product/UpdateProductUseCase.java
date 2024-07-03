package nl.fontys.s3.daclothes.business.product;

import nl.fontys.s3.daclothes.domain.UpdateProductRequest;

public interface UpdateProductUseCase {
    void updateProduct( UpdateProductRequest updateProductRequest );
}
