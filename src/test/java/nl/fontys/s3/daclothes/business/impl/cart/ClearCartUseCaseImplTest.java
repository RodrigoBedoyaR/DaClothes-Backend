package nl.fontys.s3.daclothes.business.impl.cart;

import nl.fontys.s3.daclothes.business.cart.ClearCartUseCase;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClearCartUseCaseImplTest {
    @Mock
    private GetTotalPriceUseCase getTotalPriceUseCase;


    @Test
    void clearCartTest(){
        ProductRepository productRepository = mock(ProductRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        AccessToken accessToken = mock(AccessToken.class);
        CartRepository cartRepository = mock(CartRepository.class);
        CheckIfUserIsLoggedUseCase checkIfUserIsLoggedUseCase = mock(CheckIfUserIsLoggedUseCase.class);
        MockitoAnnotations.initMocks(this);


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

        CartEntity cartEntity = CartEntity.builder()
                .id(1L)
                .cartProductList(List.of(productEntity))
                .totalPrice(productEntity.getPrice())
                .build();
        userEntity.setCart(cartEntity);

        when(accessToken.getUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(cartRepository.save(any())).thenReturn(cartEntity);
        when(getTotalPriceUseCase.totalPrice(1L)).thenReturn(0d);

    ClearCartUseCase clearCartUseCase = new ClearCartUseCaseImpl(cartRepository, accessToken, userRepository, getTotalPriceUseCase, checkIfUserIsLoggedUseCase);
    clearCartUseCase.clearCart(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(cartRepository, times(1)).save(any(CartEntity.class));
        assertEquals(0, cartEntity.getCartProductList().size());
        assertEquals(0.0, cartEntity.getTotalPrice());
    }

}