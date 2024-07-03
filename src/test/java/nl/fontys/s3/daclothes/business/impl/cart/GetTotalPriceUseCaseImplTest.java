package nl.fontys.s3.daclothes.business.impl.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.*;

import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.user.CheckIfUserIsLoggedUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
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

@ContextConfiguration(classes = {GetTotalPriceUseCaseImpl.class})
@ExtendWith(SpringExtension.class)
class GetTotalPriceUseCaseImplTest {
    @MockBean
    private AccessToken accessToken;

    @MockBean
    private CheckIfUserIsLoggedUseCase checkIfUserIsLoggedUseCase;

    @Autowired
    private GetTotalPriceUseCaseImpl getTotalPriceUseCaseImpl;

    @MockBean
    private UserRepository userRepository;


    @Test
    void testTotalPrice () {
        CartEntity cart = new CartEntity();
        cart.setCartProductList(new ArrayList<>());
        cart.setId(1L);
        cart.setTotalPrice(10.0d);

        UserEntity userEntity = new UserEntity();
        userEntity.setCart(cart);
        userEntity.setEmail("mail@mail.com");
        userEntity.setId(1L);
        userEntity.setName("name");
        userEntity.setOrders(new ArrayList<>());
        userEntity.setPassword("pw");
        userEntity.setType(new HashSet<>());
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(checkIfUserIsLoggedUseCase).checkIfUserIsLogged(Mockito.any());
        when(accessToken.getUserId()).thenReturn(1L);

        double actualTotalPriceResult = getTotalPriceUseCaseImpl.totalPrice(1L);

        verify(checkIfUserIsLoggedUseCase).checkIfUserIsLogged(Mockito.any());
        verify(userRepository).findById(Mockito.<Long>any());
        assertEquals(10.0d, actualTotalPriceResult);
    }

    @Test
    void testTotalPrice_userIsNotLoggedIn () {
        doThrow(new UserNotFoundException("Please log in")).when(checkIfUserIsLoggedUseCase)
                .checkIfUserIsLogged(Mockito.any());

        assertThrows(UserNotFoundException.class, () -> getTotalPriceUseCaseImpl.totalPrice(1L));
        verify(checkIfUserIsLoggedUseCase).checkIfUserIsLogged(Mockito.any());
    }



}
