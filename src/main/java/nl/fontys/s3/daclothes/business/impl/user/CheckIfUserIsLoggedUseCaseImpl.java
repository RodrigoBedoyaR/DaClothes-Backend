package nl.fontys.s3.daclothes.business.impl.user;

import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.user.CheckIfUserIsLoggedUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckIfUserIsLoggedUseCaseImpl implements CheckIfUserIsLoggedUseCase {
    private final AccessToken accessToken;
    @Override
    public void checkIfUserIsLogged ( AccessToken accessToken ) {
        if (accessToken.getRoles().isEmpty()){
            throw new UnauthorizedDataAccessException("YOU_HAVE_TO_LOG_IN");
        }
    }
}
