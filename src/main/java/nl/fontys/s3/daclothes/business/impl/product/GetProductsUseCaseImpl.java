package nl.fontys.s3.daclothes.business.impl.product;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.impl.converters.ProductConverter;
import nl.fontys.s3.daclothes.business.product.GetProductsUseCase;
import nl.fontys.s3.daclothes.domain.GetProductsResponse;
import nl.fontys.s3.daclothes.domain.Product;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetProductsUseCaseImpl implements GetProductsUseCase {
    private final ProductRepository productRepository;
    @Override
    @Transactional
    public GetProductsResponse getProducts () {
        List<Product> products = productRepository.findAll()
                .stream()
                .map(ProductConverter::convert)
                .toList();

        return GetProductsResponse.builder()
                .products(products)
                .build();
    }
}
