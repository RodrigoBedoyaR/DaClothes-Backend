package nl.fontys.s3.daclothes.business.product;

import nl.fontys.s3.daclothes.domain.GetProductsResponse;

public interface GetProductsByFilteringUseCase {
    GetProductsResponse getProductsByFilters(String category, String size, String condition);

}
