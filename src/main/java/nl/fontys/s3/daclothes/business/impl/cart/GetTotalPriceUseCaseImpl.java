package nl.fontys.s3.daclothes.business.impl.cart;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.cart.GetTotalPriceUseCase;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.user.CheckIfUserIsLoggedUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.CartEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetTotalPriceUseCaseImpl implements GetTotalPriceUseCase {
    private final UserRepository userRepository;
    private final CheckIfUserIsLoggedUseCase checkIfUserIsLoggedUseCase;
    private final AccessToken accessToken;
    @Override
    @Transactional
    public double totalPrice ( long userId ) {
        checkIfUserIsLoggedUseCase.checkIfUserIsLogged(accessToken);
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if(userEntityOptional.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = userEntityOptional.get();
        if(userEntity.getId() != accessToken.getUserId()){
            throw new UnauthorizedDataAccessException("Access denied!");
        }
        CartEntity cartEntity = userEntity.getCart();
        if ( cartEntity.getCartProductList() != null){
            return cartEntity.getTotalPrice();
        }
        else{
            return 0;
        }
    }
}
