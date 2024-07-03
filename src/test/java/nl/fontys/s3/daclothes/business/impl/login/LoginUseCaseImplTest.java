package nl.fontys.s3.daclothes.business.impl.login;

import nl.fontys.s3.daclothes.business.exceptions.InvalidCredentialsException;
import nl.fontys.s3.daclothes.business.login.LoginUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.daclothes.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.daclothes.domain.LoginRequest;
import nl.fontys.s3.daclothes.domain.LoginResponse;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginUseCaseImplTest {

    @Test
    void login_withRightCredentials_returnsAccessToken(){
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        AccessTokenEncoder accessTokenEncoder = mock(AccessTokenEncoder.class);

        LoginUseCase loginUseCase = new LoginUseCaseImpl(userRepository, passwordEncoder, accessTokenEncoder);

        String email = "test@email.com";
        String password = "Password";
        String encodedPassword = "encodedPassword";
        UserEntity userEntity = UserEntity.builder()
                .name("test")
                .id(1L)
                .email(email)
                .password(encodedPassword)
                .type(Collections.emptySet())
                .build();

        when(userRepository.findByEmail(email)).thenReturn(userEntity);

        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(accessTokenEncoder.encode(new AccessTokenImpl(email, userEntity.getId(), Collections.emptySet())))
                .thenReturn("accessToken");

        LoginRequest loginRequest = new LoginRequest(email, password);
        LoginResponse loginResponse = loginUseCase.login(loginRequest);

        assertNotNull(loginResponse);
        assertEquals("accessToken", loginResponse.getAccessToken());
    }

    @Test
    void login_withInvalidCredentials_throwsException() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        AccessTokenEncoder accessTokenEncoder = mock(AccessTokenEncoder.class);

        LoginUseCase loginUseCase = new LoginUseCaseImpl(userRepository, passwordEncoder, accessTokenEncoder);

        String email = "test@email.com";
        String password = "Password";
        String encodedPassword = "encodedPassword";
        UserEntity userEntity = UserEntity.builder()
                .name("test")
                .id(1L)
                .email(email)
                .password(encodedPassword)
                .type(Collections.emptySet())
                .build();

        when(userRepository.findByEmail(email)).thenReturn(userEntity);

        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        LoginRequest loginRequest = new LoginRequest(email, password);
        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.login(loginRequest));
    }
}