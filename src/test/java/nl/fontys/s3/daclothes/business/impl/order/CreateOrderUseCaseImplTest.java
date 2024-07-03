package nl.fontys.s3.daclothes.business.impl.order;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import nl.fontys.s3.daclothes.business.cart.ClearCartUseCase;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.persistence.OrderRepository;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.CartEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CreateOrderUseCaseImpl.class})
@ExtendWith(SpringExtension.class)
class CreateOrderUseCaseImplTest {
    @MockBean
    private AccessToken accessToken;

    @MockBean
    private ClearCartUseCase clearCartUseCase;

    @Autowired
    private CreateOrderUseCaseImpl createOrderUseCaseImpl;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testCreateOrder_ThrowsError () {
        when(accessToken.getRoles()).thenReturn(new HashSet<>());

        assertThrows(UnauthorizedDataAccessException.class, () -> createOrderUseCaseImpl.createOrder());
        verify(accessToken).getRoles();
    }

    @Test
    void testCreateOrder_ThrowsError2 () {
        HashSet<String> stringSet = new HashSet<>();
        stringSet.add("YOU_HAVE_TO_LOG_IN");
        when(accessToken.getRoles()).thenReturn(stringSet);

        assertThrows(UnauthorizedDataAccessException.class, () -> createOrderUseCaseImpl.createOrder());
        verify(accessToken, atLeast(1)).getRoles();
    }

    @Test
    void testCreateOrder_ThrowsError3 () {
        // Arrange
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        when(accessToken.getRoles()).thenReturn(Set.of(USER_TYPE.BUYER.toString()));

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> createOrderUseCaseImpl.createOrder());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testCreateOrder_ThrowsError4 () {
        // Arrange
        Optional<UserEntity> userEntityOptional = Optional.of(new UserEntity());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(userEntityOptional);
        when(accessToken.getRoles()).thenReturn(Set.of(USER_TYPE.BUYER.toString()));


        // Act and Assert
        assertThrows(UnauthorizedDataAccessException.class, () -> createOrderUseCaseImpl.createOrder());
        verify(userRepository).findById(Mockito.<Long>any());
    }
}
