package nl.fontys.s3.daclothes.business.impl.product;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.product.CreateProductUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.CreateProductRequest;
import nl.fontys.s3.daclothes.domain.CreateProductResponse;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.ProductEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateProductUseCaseImpl implements CreateProductUseCase {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AccessToken requestAccessToken;
    @Override
    @Transactional
    public CreateProductResponse createProduct ( CreateProductRequest request ) {
        if(requestAccessToken.getRoles().isEmpty()){
            throw new UnauthorizedDataAccessException("YOU_HAVE_TO_LOG_IN");
        }
        if (!requestAccessToken.getRoles().contains(USER_TYPE.SELLER.toString())){
            throw new UnauthorizedDataAccessException("ONLY_SELLER_CAN_CREATE_A_PRODUCT");
        }
        ProductEntity product = saveNewProduct(request);

        return CreateProductResponse.builder()
                .id(product.getId()).build();

    }

    private ProductEntity saveNewProduct(CreateProductRequest request){
        Optional<UserEntity> userEntityOptional = userRepository.findById(request.getUserId());

        if (userEntityOptional.isEmpty()){
            throw new UserNotFoundException("User not found for id: " + request.getUserId());
        }
        UserEntity user = userEntityOptional.get();

        ProductEntity newProduct = ProductEntity.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .price(request.getPrice())
                .category(request.getCategory())
                .productCondition(request.getProductCondition())
                .size(request.getSize())
                .description(request.getDescription())
                .userId(user)
                .available(true)
                .build();
        return productRepository.save(newProduct);
    }
}
