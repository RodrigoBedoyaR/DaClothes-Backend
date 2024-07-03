package nl.fontys.s3.daclothes.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.daclothes.business.login.LoginUseCase;
import nl.fontys.s3.daclothes.domain.LoginRequest;
import nl.fontys.s3.daclothes.domain.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class LoginController {
    private final LoginUseCase loginUseCase;

    @PostMapping
    public ResponseEntity<LoginResponse> login( @RequestBody @Valid LoginRequest loginRequest ){
        LoginResponse loginResponse = loginUseCase.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }
}
