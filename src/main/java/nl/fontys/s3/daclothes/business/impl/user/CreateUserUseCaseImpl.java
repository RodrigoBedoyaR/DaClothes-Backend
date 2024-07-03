package nl.fontys.s3.daclothes.business.impl.user;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.exceptions.EmailInUseException;
import nl.fontys.s3.daclothes.business.user.CreateUserUseCase;
import nl.fontys.s3.daclothes.domain.CreateUserRequest;
import nl.fontys.s3.daclothes.domain.CreateUserResponse;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.persistence.CartRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@AllArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;


    private UserEntity saveNewUser(CreateUserRequest request, String password){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new EmailInUseException();
        }
        String encodedPassword = passwordEncoder.encode(password);
        List<OrderEntity> orders = new ArrayList<>();

        UserEntity newUser = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .orders(orders)
//                .favouriteProducts(Collections.emptySet())
                .build();
        newUser.setType(Set.of(
                UserTypeEntity.builder()
                        .user_id(newUser)
                        .user_type(USER_TYPE.valueOf(request.getUserType().toUpperCase()))
                        .build()));
        List<ProductEntity> productEntityList = new ArrayList<>();
        CartEntity cart = CartEntity.builder()
                .cartProductList(productEntityList)
                .totalPrice(0)
                .build();

        newUser.setCart(cart);
        cartRepository.save(cart);
        return userRepository.save(newUser);
    }

    @Override
    @Transactional
    public CreateUserResponse createUser ( CreateUserRequest request ) {
        UserEntity user = saveNewUser(request, request.getPassword());
        return CreateUserResponse.builder()
                .userId(user.getId())
                .cartId(user.getCart().getId())
                .build();
    }
}
