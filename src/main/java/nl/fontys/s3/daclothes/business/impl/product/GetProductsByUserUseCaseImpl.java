package nl.fontys.s3.daclothes.business.impl.product;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.impl.converters.ProductConverter;
import nl.fontys.s3.daclothes.business.product.GetProductsByUserUseCase;
import nl.fontys.s3.daclothes.domain.GetProductsResponse;
import nl.fontys.s3.daclothes.domain.Product;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetProductsByUserUseCaseImpl implements GetProductsByUserUseCase {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    @Override
    @Transactional
    public GetProductsResponse getProductsByUser ( long userId ) {
        Optional<UserEntity> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        List<Product> products = productRepository.findAllByUserId_Id(userId)
                    .stream()
                    .map(ProductConverter::convert)
                    .toList();

        return GetProductsResponse.builder()
                    .products(products)
                    .build();
    }
}
