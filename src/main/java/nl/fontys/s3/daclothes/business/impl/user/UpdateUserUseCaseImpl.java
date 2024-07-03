package nl.fontys.s3.daclothes.business.impl.user;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.user.UpdateUserUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.UpdateUserRequest;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private UserRepository userRepository;
    private AccessToken accessToken;
    private final PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public void update ( UpdateUserRequest updateUserRequest ) {
        if(!accessToken.getUserId().equals(updateUserRequest.getId())){
            throw new UnauthorizedDataAccessException("CAN'T_UPDATE_ANOTHER_USER");
        }
        Optional<UserEntity> userEntityOptional = userRepository.findById(updateUserRequest.getId());
        if(userEntityOptional.isEmpty()){
            throw new UserNotFoundException("USER_NOT_FOUND");
        }
        UserEntity userEntity = userEntityOptional.get();
        updateFields(updateUserRequest, userEntity);
    }
    private void updateFields( UpdateUserRequest updateUserRequest, UserEntity userEntity ){
        userEntity.setName(updateUserRequest.getName());
        userEntity.setEmail(updateUserRequest.getEmail());
        userEntity.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
    }


}
