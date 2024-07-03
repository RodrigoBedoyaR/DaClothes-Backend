package nl.fontys.s3.daclothes.business.impl.cart;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.cart.GetCartContentUseCase;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.impl.converters.ProductConverter;
import nl.fontys.s3.daclothes.business.user.CheckIfUserIsLoggedUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.Product;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetCartContentUseCaseImpl implements GetCartContentUseCase {
    private final AccessToken accessToken;
    private final CheckIfUserIsLoggedUseCase checkIfUserIsLoggedUseCase;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public List<Product> getCartContent ( long userId ) {
        checkIfUserIsLoggedUseCase.checkIfUserIsLogged(accessToken);
        Optional<UserEntity> userEntityOptional = userRepository.findById(accessToken.getUserId());
        if(userEntityOptional.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = userEntityOptional.get();
        if(userEntity.getId() != accessToken.getUserId()){
            throw new UnauthorizedDataAccessException("Access denied!");
        }
        assert userEntity.getCart().getCartProductList() != null;
        return userEntity.getCart().getCartProductList().stream()
                .map(ProductConverter::convert)
                .toList();
    }
}
