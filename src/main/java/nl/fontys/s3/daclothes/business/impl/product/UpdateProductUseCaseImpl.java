package nl.fontys.s3.daclothes.business.impl.product;

import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.exceptions.ProductNotFoundException;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.product.UpdateProductUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.UpdateProductRequest;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.ProductEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {
    private ProductRepository repository;
    private UserRepository userRepository;
    private AccessToken accessToken;
    @Override
    public void updateProduct ( UpdateProductRequest updateProductRequest ) {
        Optional<ProductEntity> productOptional = repository.findById(updateProductRequest.getId());
        if(productOptional.isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }
        ProductEntity productEntity = productOptional.get();
        Optional<UserEntity> userEntityOptional = userRepository.findById(accessToken.getUserId());
        if(userEntityOptional.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = userEntityOptional.get();
        if (userEntity.getId() != productEntity.getUserId().getId()){
            throw new UnauthorizedDataAccessException("Access denied");
        }
        updateFields(updateProductRequest, productEntity);
    }

    private void updateFields( UpdateProductRequest request, ProductEntity entity ){
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSize(request.getSize());
        entity.setCategory(request.getCategory());
        entity.setBrand(request.getBrand());
        entity.setProductCondition(request.getProductCondition());
        entity.setPrice(request.getPrice());
        entity.setAvailable(request.isAvailable());
        repository.save(entity);
    }
}
