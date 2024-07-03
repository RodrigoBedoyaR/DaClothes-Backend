package nl.fontys.s3.daclothes.business.user;

import nl.fontys.s3.daclothes.domain.User;

import java.util.Optional;

public interface GetUserByIdUseCase {
    Optional<User> getUserById (long id);
}
