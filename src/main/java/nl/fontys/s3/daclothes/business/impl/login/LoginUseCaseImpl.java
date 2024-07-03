package nl.fontys.s3.daclothes.business.impl.login;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.daclothes.business.exceptions.InvalidCredentialsException;
import nl.fontys.s3.daclothes.business.login.LoginUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.daclothes.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.daclothes.domain.LoginRequest;
import nl.fontys.s3.daclothes.domain.LoginResponse;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;
    @Override
    @Transactional
    public LoginResponse login ( LoginRequest loginRequest ) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null){
            throw new InvalidCredentialsException();
        }

        if (!matchesPassword(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken( UserEntity user ){
        List<String> roles = user.getType().stream()
                .map(userType -> userType.getUser_type().toString())
                .toList();
        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getEmail(), user.getId(), roles)
        );

    }

}
