package nl.fontys.s3.daclothes.business.impl.product;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.impl.converters.ProductConverter;
import nl.fontys.s3.daclothes.business.product.GetProductByIdUseCase;
import nl.fontys.s3.daclothes.domain.Product;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetProductByIdUseCaseImpl implements GetProductByIdUseCase {
    private ProductRepository productRepository;
    @Override
    @Transactional
    public Optional<Product> getProductById ( long id ) {
        return productRepository.findById(id).map(ProductConverter::convert);
    }
}
