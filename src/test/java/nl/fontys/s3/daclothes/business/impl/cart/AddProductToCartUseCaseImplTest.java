package nl.fontys.s3.daclothes.business.impl.cart;

import nl.fontys.s3.daclothes.business.cart.AddProductToCartUseCase;
import nl.fontys.s3.daclothes.business.cart.GetTotalPriceUseCase;
import nl.fontys.s3.daclothes.business.user.CheckIfUserIsLoggedUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.persistence.CartRepository;
import nl.fontys.s3.daclothes.persistence.entity.CartEntity;
import nl.fontys.s3.daclothes.persistence.entity.ProductEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserTypeEntity;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddProductToCartUseCaseImplTest {
    @Mock
    private CheckIfUserIsLoggedUseCase checkIfUserIsLoggedUseCase;

    @Mock
    private GetTotalPriceUseCase getTotalPriceUseCase;

    @Test
    void addProductToCart(){
        ProductRepository productRepository = mock(ProductRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        AccessToken accessToken = mock(AccessToken.class);
        CartRepository cartRepository = mock(CartRepository.class);

        MockitoAnnotations.initMocks(this);


        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("Rodrigo")
                .email("email@email.com")
                .password("Password123").build();

        CartEntity cartEntity = CartEntity.builder()
                .id(1L)
                .cartProductList(new ArrayList<>())
                .totalPrice(0)
                .build();
        userEntity.setCart(cartEntity);

        UserTypeEntity userTypeEntity = UserTypeEntity
                .builder()
                .id(1L)
                .user_type(USER_TYPE.BUYER)
                .user_id(userEntity).build();
        userEntity.setType(Set.of(userTypeEntity));


        UserEntity seller = UserEntity.builder()
                .id(2L)
                .name("Rodrigo")
                .email("email2@email.com")
                .password("Password123").build();
        UserTypeEntity sellerType = UserTypeEntity
                .builder()
                .id(2L)
                .user_type(USER_TYPE.BUYER)
                .user_id(userEntity).build();
        userEntity.setType(Set.of(sellerType));

        ProductEntity productEntity = ProductEntity.builder()
                .id(1)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(20.0)
                .name("Airforce Max")
                .size("46")
                .userId(seller)
                .available(true)
                .build();

        when(accessToken.getRoles()).thenReturn(Set.of("BUYER"));
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(getTotalPriceUseCase.totalPrice(1L)).thenReturn(20.0);
        when(cartRepository.save(cartEntity)).thenReturn(cartEntity);
        when(accessToken.getUserId()).thenReturn(userEntity.getId());

        AddProductToCartUseCase addProductToCartUseCase = new AddProductToCartUseCaseImpl(accessToken, productRepository, userRepository, cartRepository, getTotalPriceUseCase, checkIfUserIsLoggedUseCase);
        addProductToCartUseCase.addToCart(1);

        assertEquals(1, cartEntity.getCartProductList().size());
    }


}