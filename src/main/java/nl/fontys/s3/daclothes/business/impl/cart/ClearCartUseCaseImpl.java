package nl.fontys.s3.daclothes.business.impl.cart;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.cart.ClearCartUseCase;
import nl.fontys.s3.daclothes.business.cart.GetTotalPriceUseCase;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.user.CheckIfUserIsLoggedUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.persistence.CartRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.CartEntity;
import nl.fontys.s3.daclothes.persistence.entity.ProductEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClearCartUseCaseImpl implements ClearCartUseCase {
    private final CartRepository cartRepository;
    private final AccessToken accessToken;
    private final UserRepository userRepository;
    private final GetTotalPriceUseCase getTotalPriceUseCase;    
    private final CheckIfUserIsLoggedUseCase checkIfUserIsLoggedUseCase;
    @Override
    @Transactional
    public void clearCart (long userId) {
        //extract duplicate code to method
        checkIfUserIsLoggedUseCase.checkIfUserIsLogged(accessToken);
        Optional<UserEntity> userOptional = userRepository.findById(accessToken.getUserId());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        UserEntity userEntity = userOptional.get();
        if(userEntity.getId() != accessToken.getUserId()){
            throw new UnauthorizedDataAccessException("Access denied!");
        }
        CartEntity cartEntity = userEntity.getCart();
        List<ProductEntity> productEntityList = new ArrayList<>();
        cartEntity.setCartProductList(productEntityList);
        cartEntity.setTotalPrice(0);
        cartRepository.save(cartEntity);
    }
}
