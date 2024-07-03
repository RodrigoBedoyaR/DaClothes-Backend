package nl.fontys.s3.daclothes.business.impl.user;

import jakarta.persistence.Access;
import nl.fontys.s3.daclothes.business.user.GetUserByIdUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.domain.User;
import nl.fontys.s3.daclothes.persistence.entity.CartEntity;
import nl.fontys.s3.daclothes.persistence.entity.OrderEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserTypeEntity;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetUserByIdUseCaseImplTest {

    @Test
    void getUserById(){
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
        CartEntity cartEntity = CartEntity.builder()
                .cartProductList(Collections.emptyList())
                .totalPrice(0)
                .id(1L).build();
        userEntity.setCart(cartEntity);

        OrderEntity orderEntity = OrderEntity.builder()
                .id(1L)
                .order_product_list(Collections.emptyList())
                .created_at(new Date(2020, 12,12))
                .total_price(0)
                .build();
        userEntity.setOrders(List.of(orderEntity));

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(accessToken.getUserId()).thenReturn(userEntity.getId());

        GetUserByIdUseCase getUserByIdUseCase = new GetUserByIdUseCaseImpl(userRepository, accessToken);
        Optional<User> actualUser = getUserByIdUseCase.getUserById(1L);

        Mockito.verify(userRepository, Mockito.times(2)).findById(1L);

        assertTrue(actualUser.isPresent());
        assertNotNull(actualUser.get());
        assertEquals(userEntity.getId(), actualUser.get().getId());
        assertEquals(userEntity.getName(), actualUser.get().getName());


    }

}