package nl.fontys.s3.daclothes.business.impl.order;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.business.impl.converters.OrderConverter;
import nl.fontys.s3.daclothes.business.order.GetOrdersFromUserUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.Order;
import nl.fontys.s3.daclothes.persistence.OrderRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetOrdersFromUserUseCaseImpl implements GetOrdersFromUserUseCase {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AccessToken accessToken;
    
    @Override
    @Transactional
    public List<Order> getOrdersByUser () {
        Optional<UserEntity> userEntityOptional = userRepository.findById(accessToken.getUserId());
        if(userEntityOptional.isEmpty()){
            throw new UserNotFoundException("USER_NOT_FOUND");
        }
        UserEntity userEntity = userEntityOptional.get();
        assert userEntity.getOrders() != null;
        return userEntity.getOrders().stream().map(OrderConverter::convert).toList();
    }
}
