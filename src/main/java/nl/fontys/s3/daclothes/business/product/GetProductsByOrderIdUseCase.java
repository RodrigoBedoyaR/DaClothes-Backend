package nl.fontys.s3.daclothes.business.product;

import nl.fontys.s3.daclothes.domain.GetProductsResponse;

public interface GetProductsByOrderIdUseCase {
    GetProductsResponse getProductsByOrderId( long orderId);
}
