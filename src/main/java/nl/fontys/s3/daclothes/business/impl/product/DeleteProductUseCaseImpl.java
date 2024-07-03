package nl.fontys.s3.daclothes.business.impl.product;

import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.exceptions.ProductNotFoundException;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.product.DeleteProductUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.ProductEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeleteProductUseCaseImpl implements DeleteProductUseCase {
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private AccessToken accessToken;
    @Override
    public void deleteProduct ( long productId ) {
        Optional<ProductEntity> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }
        ProductEntity productEntity = productOptional.get();
        Optional<UserEntity> userEntityOptional = userRepository.findById(productEntity.getUserId().getId());
        if(userEntityOptional.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = userEntityOptional.get();
        if (userEntity.getId() != productEntity.getUserId().getId()){
            throw new UnauthorizedDataAccessException("Access denied");
        }
        this.productRepository.deleteById(productId);
    }
}
