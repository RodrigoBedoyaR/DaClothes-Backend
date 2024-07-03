package nl.fontys.s3.daclothes.business.impl.cart;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.user.CheckIfUserIsLoggedUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.Product;
import nl.fontys.s3.daclothes.persistence.entity.CartEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {GetCartContentUseCaseImpl.class})
@ExtendWith(SpringExtension.class)
class GetCartContentUseCaseImplTest {
    @MockBean
    private AccessToken accessToken;

    @MockBean
    private CheckIfUserIsLoggedUseCase checkIfUserIsLoggedUseCase;

    @Autowired
    private GetCartContentUseCaseImpl getCartContentUseCaseImpl;

    @MockBean
    private UserRepository userRepository;


    @Test
    void testGetCartContent () {
        when(accessToken.getUserId()).thenReturn(1L);
        doNothing().when(checkIfUserIsLoggedUseCase).checkIfUserIsLogged(Mockito.any());

        CartEntity cart = new CartEntity();
        cart.setCartProductList(new ArrayList<>());
        cart.setId(1L);
        cart.setTotalPrice(10.0d);

        UserEntity userEntity = new UserEntity();
        userEntity.setCart(cart);
        userEntity.setEmail("mail@mail.com");
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setOrders(new ArrayList<>());
        userEntity.setPassword("password");
        userEntity.setType(new HashSet<>());
        Optional<UserEntity> userEntityOptional = Optional.of(userEntity);


        when(userRepository.findById(Mockito.<Long>any())).thenReturn(userEntityOptional);

        List<Product> actualCartContent = getCartContentUseCaseImpl.getCartContent(1L);

        verify(checkIfUserIsLoggedUseCase).checkIfUserIsLogged(Mockito.any());
        verify(userRepository).findById(Mockito.<Long>any());
        assertTrue(actualCartContent.isEmpty());
    }

    @Test
    void testGetCartContentThrowsUserNotFoundException () {
        when(accessToken.getUserId()).thenReturn(1L);
        doNothing().when(checkIfUserIsLoggedUseCase).checkIfUserIsLogged(Mockito.any());

        when(userRepository.findById(Mockito.<Long>any())).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> getCartContentUseCaseImpl.getCartContent(1L));
        verify(checkIfUserIsLoggedUseCase).checkIfUserIsLogged(Mockito.any());
        verify(accessToken).getUserId();
        verify(userRepository).findById(Mockito.<Long>any());
    }
}
