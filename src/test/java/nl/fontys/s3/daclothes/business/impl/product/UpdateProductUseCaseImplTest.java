package nl.fontys.s3.daclothes.business.impl.product;

import nl.fontys.s3.daclothes.business.product.UpdateProductUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.domain.UpdateProductRequest;
import nl.fontys.s3.daclothes.persistence.entity.ProductEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserTypeEntity;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class UpdateProductUseCaseImplTest {

    @Test
    void updateProduct () {
        ProductRepository productRepository = mock(ProductRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        AccessToken accessToken = mock(AccessToken.class);
        long productIdToUpdate = 1L;



        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("Rodrigo")
                .email("email@email.com")
                .password("Password123").build();
        UserTypeEntity userTypeEntity = UserTypeEntity
                .builder()
                .id(1L)
                .user_type(USER_TYPE.SELLER)
                .user_id(userEntity).build();
        userEntity.setType(Set.of(userTypeEntity));
        ProductEntity existingProduct = ProductEntity.builder()
                .id(productIdToUpdate)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .userId(userEntity)
                .build();

        when(productRepository.findById(productIdToUpdate)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(existingProduct);
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(accessToken.getUserId()).thenReturn(userEntity.getId());


        UpdateProductUseCase updateProductUseCase = new UpdateProductUseCaseImpl(productRepository, userRepository, accessToken);

        UpdateProductRequest updatedProduct = UpdateProductRequest.builder()
                .id(productIdToUpdate)
                .brand("Adidas")
                .category("Shoes")
                .productCondition("Used")
                .description("Really nice shoes. I don't use them anymore.")
                .price(20)
                .name("Superstar")
                .size("46")
                .build();
        updateProductUseCase.updateProduct(updatedProduct);
        Mockito.verify(productRepository, Mockito.times(1)).findById(productIdToUpdate);

    }
}