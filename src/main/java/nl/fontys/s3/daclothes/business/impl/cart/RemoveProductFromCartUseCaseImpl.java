package nl.fontys.s3.daclothes.business.impl.cart;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.cart.GetTotalPriceUseCase;
import nl.fontys.s3.daclothes.business.cart.RemoveProductFromCartUseCase;
import nl.fontys.s3.daclothes.business.exceptions.ProductNotFoundException;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.user.CheckIfUserIsLoggedUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.persistence.CartRepository;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.CartEntity;
import nl.fontys.s3.daclothes.persistence.entity.ProductEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RemoveProductFromCartUseCaseImpl implements RemoveProductFromCartUseCase {
    private final CartRepository cartRepository;
    private final AccessToken accessToken;
    private final UserRepository userRepository;
    private final GetTotalPriceUseCase getTotalPriceUseCase;
    private final CheckIfUserIsLoggedUseCase checkIfUserIsLoggedUseCase;
    private final ProductRepository productRepository;
    @Override
    @Transactional
    public void removeFromCart ( long productId ) {
        checkIfUserIsLoggedUseCase.checkIfUserIsLogged(accessToken);
        Optional<UserEntity> userOptional = userRepository.findById(accessToken.getUserId());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        Optional<ProductEntity> productEntityOptional = productRepository.findById(productId);
        if (productEntityOptional.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        UserEntity userEntity = userOptional.get();
        if(userEntity.getId() != accessToken.getUserId()){
            throw new UnauthorizedDataAccessException("Access denied!");
        }
        CartEntity cartEntity = userEntity.getCart();
        ProductEntity productEntity = productEntityOptional.get();
        assert cartEntity.getCartProductList() != null;
        cartEntity.getCartProductList().remove(productEntity);

        cartEntity.setTotalPrice(getTotalPriceUseCase.totalPrice(userEntity.getId()) - productEntity.getPrice());
        cartRepository.save(cartEntity);

    }
}
