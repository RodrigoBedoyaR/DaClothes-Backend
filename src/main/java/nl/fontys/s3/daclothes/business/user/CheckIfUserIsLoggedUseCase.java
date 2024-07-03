package nl.fontys.s3.daclothes.business.user;

import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;

public interface CheckIfUserIsLoggedUseCase {
    void checkIfUserIsLogged( AccessToken accessToken );
}
