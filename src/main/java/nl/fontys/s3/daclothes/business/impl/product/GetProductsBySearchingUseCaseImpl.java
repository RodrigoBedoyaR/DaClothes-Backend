package nl.fontys.s3.daclothes.business.impl.product;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.impl.converters.ProductConverter;
import nl.fontys.s3.daclothes.business.product.GetProductsBySearchingUseCase;
import nl.fontys.s3.daclothes.domain.GetProductsResponse;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetProductsBySearchingUseCaseImpl implements GetProductsBySearchingUseCase {
    private final ProductRepository productRepository;
    @Override
    @Transactional
    public GetProductsResponse getProductsBySearching ( String search ) {
        return GetProductsResponse.builder().products(productRepository.findAllByNameOrDescriptionOrUserNameContainingIgnoreCase(search)
                .stream().map(ProductConverter::convert).toList()).build();
    }
}
