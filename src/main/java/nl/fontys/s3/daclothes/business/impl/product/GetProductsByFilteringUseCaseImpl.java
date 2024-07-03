package nl.fontys.s3.daclothes.business.impl.product;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.impl.converters.ProductConverter;
import nl.fontys.s3.daclothes.business.product.GetProductsByFilteringUseCase;
import nl.fontys.s3.daclothes.domain.GetProductsResponse;
import nl.fontys.s3.daclothes.domain.Product;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.entity.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@AllArgsConstructor
@Service
public class GetProductsByFilteringUseCaseImpl implements GetProductsByFilteringUseCase {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public GetProductsResponse getProductsByFilters ( String category, String size, String condition ) {
        List<ProductEntity> productList = null;
        if(category != null && size != null && condition != null){
            productList = (productRepository.findByCategoryAndProductConditionAndSizeContainingIgnoreCase(category, condition, size));
        } else if (category != null && size != null) {
            productList = productRepository.findByCategoryAndSizeContainingIgnoreCase(category, size);
        } else if (condition != null && size != null) {
            productList = productRepository.findByProductConditionAndSizeContainingIgnoreCase(condition, size);
        } else if (category != null && condition != null) {
            productList = productRepository.findByProductConditionAndCategoryContainingIgnoreCase(condition, category);
        } else if (category != null) {
            productList = productRepository.findByCategoryContainingIgnoreCase(category);
        } else if (size != null) {
            productList = productRepository.findBySizeContainingIgnoreCase(size);
        } else if (condition != null) {
            productList = productRepository.findByProductConditionContainingIgnoreCase(condition);
        }
        if (productList == null){
            return GetProductsResponse.builder().products(Collections.emptyList()).build();
        }
        List<Product> convertedProduct = productList.stream()
                .map(ProductConverter::convert).toList();
        return GetProductsResponse.builder().products(convertedProduct).build();
    }
}
