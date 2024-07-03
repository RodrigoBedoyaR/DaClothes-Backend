package nl.fontys.s3.daclothes.business.impl.user;

import nl.fontys.s3.daclothes.business.user.CreateUserUseCase;
import nl.fontys.s3.daclothes.domain.CreateUserRequest;
import nl.fontys.s3.daclothes.domain.CreateUserResponse;
import nl.fontys.s3.daclothes.persistence.CartRepository;
import nl.fontys.s3.daclothes.persistence.entity.CartEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.UserTypeEntity;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Set;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateUserUseCaseImplTest {
    @Test
    void createUser_savesUserInDB(){
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        CartRepository cartRepository = mock(CartRepository.class);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("Rodrigo")
                .email("email@email.com")
                .password(passwordEncoder.encode("myPassword"))
                .build();
        UserTypeEntity userTypeEntity = UserTypeEntity
                .builder()
                .id(1L)
                .user_id(userEntity).build();
        userEntity.setType(Set.of(userTypeEntity));

        CartEntity cartEntity = CartEntity.builder()
                .cartProductList(Collections.emptyList())
                .totalPrice(0)
                .id(1L).build();
        userEntity.setCart(cartEntity);

        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);

        CreateUserUseCase createUserUseCase = new CreateUserUseCaseImpl(userRepository, cartRepository, passwordEncoder);
        CreateUserRequest newUser = CreateUserRequest.builder()
                .name("Rodrigo")
                .email("email@email.com")
                .password(passwordEncoder.encode("myPassword"))
                .userType("buyer").build();
        CreateUserResponse response = createUserUseCase.createUser(newUser);
        assertEquals(userEntity.getId(), response.getUserId());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));


    }


}