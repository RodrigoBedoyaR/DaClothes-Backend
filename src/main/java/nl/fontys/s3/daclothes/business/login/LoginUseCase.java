package nl.fontys.s3.daclothes.business.login;

import nl.fontys.s3.daclothes.domain.LoginRequest;
import nl.fontys.s3.daclothes.domain.LoginResponse;

public interface LoginUseCase {
    LoginResponse login( LoginRequest loginRequest );
}
