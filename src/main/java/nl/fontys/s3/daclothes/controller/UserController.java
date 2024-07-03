package nl.fontys.s3.daclothes.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.user.CreateUserUseCase;
import nl.fontys.s3.daclothes.business.user.GetUserByIdUseCase;
import nl.fontys.s3.daclothes.business.user.UpdateUserUseCase;
import nl.fontys.s3.daclothes.domain.CreateUserRequest;
import nl.fontys.s3.daclothes.domain.CreateUserResponse;
import nl.fontys.s3.daclothes.domain.UpdateUserRequest;
import nl.fontys.s3.daclothes.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin("http://localhost:5173")
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser ( @RequestBody @Valid CreateUserRequest request ){
        CreateUserResponse response = createUserUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id")final long id){
        final Optional<User> userOptional = getUserByIdUseCase.getUserById(id);
        if (userOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(userOptional.get());
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateUser( @PathVariable(value = "id")final long id, @RequestBody @Valid UpdateUserRequest updateUserRequest){
        updateUserRequest.setId(id);
        updateUserUseCase.update(updateUserRequest);
        return ResponseEntity.noContent().build();
    }
}
