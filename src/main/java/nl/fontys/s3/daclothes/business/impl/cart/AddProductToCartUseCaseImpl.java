package nl.fontys.s3.daclothes.business.impl.cart;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.cart.AddProductToCartUseCase;
import nl.fontys.s3.daclothes.business.cart.GetTotalPriceUseCase;
import nl.fontys.s3.daclothes.business.exceptions.ProductNotFoundException;
import nl.fontys.s3.daclothes.business.exceptions.ProductUnavailableException;
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
public class AddProductToCartUseCaseImpl implements AddProductToCartUseCase {
    private final AccessToken accessToken;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final GetTotalPriceUseCase getTotalPriceUseCase;
    private final CheckIfUserIsLoggedUseCase checkIfUserIsLoggedUseCase;

    @Override
    @Transactional
    public void addToCart ( long productId ) {
        checkIfUserIsLoggedUseCase.checkIfUserIsLogged(accessToken);

        Optional<UserEntity> userOptional = userRepository.findById(accessToken.getUserId());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        Optional<ProductEntity> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }
        ProductEntity product = productOptional.get();

        if (!product.isAvailable()) {
            throw new ProductUnavailableException("Sorry! product unavailable");
        }
        UserEntity user = userOptional.get();
        if(user.getId() != accessToken.getUserId()){
            throw new UnauthorizedDataAccessException("Access denied!");
        }
        CartEntity cartEntity = user.getCart();

        cartEntity.setTotalPrice(getTotalPriceUseCase.totalPrice(user.getId()) + product.getPrice());

        cartEntity.getCartProductList().add(product);


        cartRepository.save(cartEntity);
    }
}
