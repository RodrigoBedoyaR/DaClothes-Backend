package nl.fontys.s3.daclothes.business.impl.cart;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import nl.fontys.s3.daclothes.business.cart.GetTotalPriceUseCase;
import nl.fontys.s3.daclothes.business.user.CheckIfUserIsLoggedUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.persistence.CartRepository;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.CartEntity;
import nl.fontys.s3.daclothes.persistence.entity.ProductEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {RemoveProductFromCartUseCaseImpl.class})
@ExtendWith(SpringExtension.class)
class RemoveProductFromCartUseCaseImplTest {
    @MockBean
    private AccessToken accessToken;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private CheckIfUserIsLoggedUseCase checkIfUserIsLoggedUseCase;

    @MockBean
    private GetTotalPriceUseCase getTotalPriceUseCase;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private RemoveProductFromCartUseCaseImpl removeProductFromCartUseCaseImpl;

    @MockBean
    private UserRepository userRepository;


    @Test
    void testRemoveFromCart () {
        CartEntity cartEntity = new CartEntity();
        cartEntity.setCartProductList(new ArrayList<>());
        cartEntity.setId(1L);
        cartEntity.setTotalPrice(10.0d);
        when(cartRepository.save(Mockito.any())).thenReturn(cartEntity);
        when(accessToken.getUserId()).thenReturn(1L);

        CartEntity cart = new CartEntity();
        cart.setCartProductList(new ArrayList<>());
        cart.setId(1L);
        cart.setTotalPrice(10.0d);

        UserEntity userEntity = new UserEntity();
        userEntity.setCart(cart);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setOrders(new ArrayList<>());
        userEntity.setPassword("iloveyou");
        userEntity.setType(new HashSet<>());
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(getTotalPriceUseCase.totalPrice(anyLong())).thenReturn(10.0d);
        doNothing().when(checkIfUserIsLoggedUseCase).checkIfUserIsLogged(Mockito.any());

        CartEntity cart2 = new CartEntity();
        cart2.setCartProductList(new ArrayList<>());
        cart2.setId(1L);
        cart2.setTotalPrice(10.0d);

        UserEntity userId = new UserEntity();
        userId.setCart(cart2);
        userId.setEmail("mail@example.org");
        userId.setId(1L);
        userId.setName("Name");
        userId.setOrders(new ArrayList<>());
        userId.setPassword("pw");
        userId.setType(new HashSet<>());

        ProductEntity productEntity = new ProductEntity();
        productEntity.setAvailable(true);
        productEntity.setBrand("Brand");
        productEntity.setCategory("Category");
        productEntity.setDescription("The characteristics of someone or something");
        productEntity.setId(1L);
        productEntity.setName("Name");
        productEntity.setOrders(new ArrayList<>());
        productEntity.setPrice(10.0d);
        productEntity.setProductCondition("Product Condition");
        productEntity.setSize("Size");
        productEntity.setUserId(userId);
        Optional<ProductEntity> ofResult2 = Optional.of(productEntity);
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        removeProductFromCartUseCaseImpl.removeFromCart(1L);

        verify(getTotalPriceUseCase).totalPrice(1L);
        verify(checkIfUserIsLoggedUseCase).checkIfUserIsLogged(Mockito.any());
        verify(accessToken, atLeast(1)).getUserId();
        verify(productRepository).findById(Mockito.<Long>any());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(cartRepository).save(Mockito.any());
    }

}
