package nl.fontys.s3.daclothes.business.impl.order;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.Order;
import nl.fontys.s3.daclothes.persistence.OrderRepository;
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

@ContextConfiguration(classes = {GetOrdersFromUserUseCaseImpl.class})
@ExtendWith(SpringExtension.class)
class GetOrdersFromUserUseCaseImplTest {
    @MockBean
    private AccessToken accessToken;

    @Autowired
    private GetOrdersFromUserUseCaseImpl getOrdersFromUserUseCaseImpl;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testGetOrdersByUser () {
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
        when(accessToken.getUserId()).thenReturn(1L);

        List<Order> actualOrdersByUser = getOrdersFromUserUseCaseImpl.getOrdersByUser();

        verify(accessToken).getUserId();
        verify(userRepository).findById(Mockito.<Long>any());
        assertTrue(actualOrdersByUser.isEmpty());
    }
}
