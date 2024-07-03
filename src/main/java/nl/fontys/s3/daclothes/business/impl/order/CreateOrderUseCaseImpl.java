package nl.fontys.s3.daclothes.business.impl.order;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.cart.ClearCartUseCase;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.order.CreateOrderUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.CreateOrderResponse;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.persistence.OrderRepository;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.CartEntity;
import nl.fontys.s3.daclothes.persistence.entity.OrderEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AccessToken accessToken;
    private final UserRepository userRepository;
    private final ClearCartUseCase clearCartUseCase;

    @Override
    @Transactional
    public CreateOrderResponse createOrder () {
        if(accessToken.getRoles().isEmpty()){
            throw new UnauthorizedDataAccessException("YOU_HAVE_TO_LOG_IN");
        }
        if (!accessToken.getRoles().contains(USER_TYPE.BUYER.toString())){
            throw new UnauthorizedDataAccessException("ONLY_BUYER_CAN_CREATE_AN_ORDER");
        }
        Optional<UserEntity> userEntityOptional = userRepository.findById(accessToken.getUserId());
        if(userEntityOptional.isEmpty()){
            throw new UserNotFoundException("USER_NOT_FOUND");
        }
        UserEntity user = userEntityOptional.get();
        CartEntity cartEntity = user.getCart();
        if(cartEntity == null || cartEntity.getCartProductList().isEmpty()){
            throw new UnauthorizedDataAccessException("CART_NOT_FOUND");
        }
        OrderEntity orderEntity = OrderEntity.builder()
                .created_at(new Date())
                .total_price(cartEntity.getTotalPrice())
                .order_product_list(cartEntity.getCartProductList())
                .build();

        orderRepository.save(orderEntity);
        user.getOrders().add(orderEntity);
        userRepository.save(user);
        cartEntity.getCartProductList().forEach(productEntity -> {
            productEntity.setAvailable(false);
        productRepository.save(productEntity);
        });
        clearCartUseCase.clearCart(user.getId());
        return CreateOrderResponse.builder()
                .id(orderEntity.getId())
                .build();
    }
}
