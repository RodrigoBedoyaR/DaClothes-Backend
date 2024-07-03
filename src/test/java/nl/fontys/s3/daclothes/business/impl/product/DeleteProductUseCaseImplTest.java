package nl.fontys.s3.daclothes.business.impl.product;

import nl.fontys.s3.daclothes.business.exceptions.ProductNotFoundException;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.product.DeleteProductUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
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

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class DeleteProductUseCaseImplTest {
    @Test
    void deleteProductWithMockito () {
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        AccessToken accessToken = Mockito.mock(AccessToken.class);

        long productIdToDelete = 1L;
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .password("pw")
                .build();
        UserTypeEntity userTypeEntity = UserTypeEntity.builder()
                .id(1L)
                .user_type(USER_TYPE.SELLER)
                .user_id(userEntity).build();
        userTypeEntity.setUser_type(userTypeEntity.getUser_type());
        ProductEntity productEntity = ProductEntity.builder()
                .id(productIdToDelete)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .userId(userEntity)
                .build();

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(productRepository.findById(productIdToDelete)).thenReturn(Optional.of(productEntity));
        when(accessToken.getRoles()).thenReturn(Set.of("ADMIN"));

        doNothing().when(productRepository).deleteById(productIdToDelete);

        DeleteProductUseCase deleteProductUseCase = new DeleteProductUseCaseImpl(productRepository, userRepository, accessToken);

        deleteProductUseCase.deleteProduct(productEntity.getId());

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(1L);

        verify(productRepository).deleteById(productEntity.getId());
    }

    @Test
    void deleteProductThrowsUserNotfoundError () {
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        AccessToken accessToken = Mockito.mock(AccessToken.class);

        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .userId(new UserEntity())
                .build();

        Optional<UserEntity> userEntityOptional = Optional.empty();
        Optional<ProductEntity> productEntityOptional = Optional.of(productEntity);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(userEntityOptional);
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(productEntityOptional);
        when(accessToken.getRoles()).thenReturn(Set.of(USER_TYPE.ADMIN.toString()));
        DeleteProductUseCase deleteProductUseCase = new DeleteProductUseCaseImpl(productRepository, userRepository, accessToken);


        assertThrows(UserNotFoundException.class, () -> deleteProductUseCase.deleteProduct(1L));
    }

    @Test
    void deleteProductThrowsProductNotFoundError () {
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        AccessToken accessToken = Mockito.mock(AccessToken.class);

        Optional<ProductEntity> productEntityOptional = Optional.empty();
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(productEntityOptional);

        DeleteProductUseCase deleteProductUseCase = new DeleteProductUseCaseImpl(productRepository, userRepository, accessToken);

        assertThrows(ProductNotFoundException.class, () -> deleteProductUseCase.deleteProduct(1L));
    }

    @Test
    void deleteProductThrowsUnauthorizedDataAccessException () {
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        AccessToken accessToken = Mockito.mock(AccessToken.class);

        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .userId(new UserEntity())
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(20L)
                .name("name")
                .email("email@email.com")
                .password("pw")
                .build();
        UserTypeEntity userTypeEntity = UserTypeEntity.builder()
                .id(1L)
                .user_type(USER_TYPE.SELLER)
                .user_id(userEntity).build();
        userTypeEntity.setUser_type(userTypeEntity.getUser_type());


        Optional<ProductEntity> productEntityOptional = Optional.of(productEntity);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(userEntity));
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(productEntityOptional);
        when(accessToken.getRoles()).thenReturn(Set.of(USER_TYPE.BUYER.toString()));

        DeleteProductUseCase deleteProductUseCase = new DeleteProductUseCaseImpl(productRepository, userRepository, accessToken);

        assertThrows(UnauthorizedDataAccessException.class, () -> deleteProductUseCase.deleteProduct(1L));
    }
}