package nl.fontys.s3.daclothes.business.user;

import nl.fontys.s3.daclothes.domain.UpdateUserRequest;

public interface UpdateUserUseCase {
    void update ( UpdateUserRequest updateUserRequest );
}
