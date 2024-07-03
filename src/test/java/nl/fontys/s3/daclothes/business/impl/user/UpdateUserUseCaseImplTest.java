package nl.fontys.s3.daclothes.business.impl.user;

import nl.fontys.s3.daclothes.business.user.UpdateUserUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.domain.UpdateUserRequest;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateUserUseCaseImplTest {
        @Test
    void anotherTest(){
        UserRepository userRepository = mock(UserRepository.class);
        AccessToken accessToken = mock(AccessToken.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        UpdateUserUseCase updateUserUseCase = new UpdateUserUseCaseImpl(userRepository, accessToken, passwordEncoder);

        Long userId = 1L;
        String name = "New Name";
        String email = "new@email.com";
        String password = "newPassword";
        USER_TYPE userType = USER_TYPE.BUYER;

        when(accessToken.getUserId()).thenReturn(userId);

        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        UpdateUserRequest updateUserRequest = new UpdateUserRequest(userId, name, email, password);
        updateUserUseCase.update(updateUserRequest);

        verify(userRepository, times(1)).findById(userId);
        assertEquals(name, existingUser.getName());
        assertEquals(email, existingUser.getEmail());
//        assertTrue(passwordEncoder.matches(password, existingUser.getPassword()));
//        assertEquals(1, existingUser.getType().size());
//        assertEquals(userType, existingUser.getType().iterator().next().getUser_type());
    }
}