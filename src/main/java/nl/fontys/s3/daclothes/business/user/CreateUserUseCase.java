package nl.fontys.s3.daclothes.business.user;


import nl.fontys.s3.daclothes.domain.CreateUserRequest;
import nl.fontys.s3.daclothes.domain.CreateUserResponse;

public interface CreateUserUseCase {
    CreateUserResponse createUser ( CreateUserRequest request );
}
