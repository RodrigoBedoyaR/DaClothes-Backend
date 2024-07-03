package nl.fontys.s3.daclothes.business.impl.product;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.exceptions.OrderNotFoundException;
import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.business.impl.converters.ProductConverter;
import nl.fontys.s3.daclothes.business.product.GetProductsByOrderIdUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.GetProductsResponse;
import nl.fontys.s3.daclothes.persistence.OrderRepository;
import nl.fontys.s3.daclothes.persistence.entity.OrderEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetProductsByOrderIdUseCaseImpl implements GetProductsByOrderIdUseCase {
    private final OrderRepository orderRepository;
    private final AccessToken accessToken;
    @Override
    @Transactional
    public GetProductsResponse getProductsByOrderId ( long orderId ) {
        if (accessToken.getRoles().isEmpty()){
            throw new UnauthorizedDataAccessException("PLEASE_LOGIN");
        }
        Optional<OrderEntity> orderEntityOptional = orderRepository.findById(orderId);
        if(orderEntityOptional.isEmpty()){
            throw new OrderNotFoundException("ORDER_NOT_FOUND");
        }
        OrderEntity orderEntity = orderEntityOptional.get();

        return GetProductsResponse.builder()
                .products(orderEntity.getOrder_product_list().stream().map(ProductConverter::convert).toList())
                .build();
    }
}
