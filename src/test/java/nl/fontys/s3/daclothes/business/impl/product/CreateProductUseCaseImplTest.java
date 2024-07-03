package nl.fontys.s3.daclothes.business.impl.product;

import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.product.CreateProductUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.CreateProductRequest;
import nl.fontys.s3.daclothes.domain.CreateProductResponse;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.persistence.entity.ProductEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserTypeEntity;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateProductUseCaseImplTest {

    @Test
    void createProduct_throwsError_becauseUserIsABuyer () {
        ProductRepository productRepository = mock(ProductRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        AccessToken accessToken = mock(AccessToken.class);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("Rodrigo")
                .email("email@email.com")
                .password("Password123").build();
        UserTypeEntity userTypeEntity = UserTypeEntity
                .builder()
                .id(1L)
                .user_type(USER_TYPE.BUYER)
                .user_id(userEntity).build();
        userEntity.setType(Set.of(userTypeEntity));

        ProductEntity productEntity = ProductEntity.builder()
                .id(1)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .userId(userEntity)
                .build();


        when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(productEntity);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(accessToken.getRoles()).thenReturn(Set.of("BUYER"));

        CreateProductUseCase createProductUseCase = new CreateProductUseCaseImpl(productRepository, userRepository, accessToken);
        CreateProductRequest newProduct = CreateProductRequest.builder()
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .userId(userEntity.getId())
                .build();
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> {
            createProductUseCase.createProduct(newProduct);
        });
        assertTrue(exception.getMessage().contains("ONLY_SELLER_CAN_CREATE_A_PRODUCT"));
    }
    @Test
    void createProduct_saves_newProductInDB () {
        ProductRepository productRepository = mock(ProductRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        AccessToken accessToken = mock(AccessToken.class);
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
        ProductEntity productEntity = ProductEntity.builder()
                .id(1)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .userId(userEntity)
                .build();


        when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(productEntity);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(accessToken.getRoles()).thenReturn(Set.of(USER_TYPE.SELLER.toString()));

        assertTrue(userEntity.getType().toString().contains("SELLER"));

        CreateProductUseCase createProductUseCase = new CreateProductUseCaseImpl(productRepository, userRepository, accessToken);
        CreateProductRequest newProduct = CreateProductRequest.builder()
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .userId(userEntity.getId())
                .build();
        assertTrue(accessToken.getRoles().contains("SELLER"));
        CreateProductResponse response = createProductUseCase.createProduct(newProduct);
        assertEquals(productEntity.getId(), response.getId());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(ProductEntity.class));
    }


}