package nl.fontys.s3.daclothes.business.product;

import nl.fontys.s3.daclothes.domain.GetProductsResponse;

public interface GetProductsBySearchingUseCase {
    GetProductsResponse getProductsBySearching(String search);
}
