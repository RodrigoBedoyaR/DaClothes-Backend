package nl.fontys.s3.daclothes.business.impl.product;

import nl.fontys.s3.daclothes.business.product.GetProductByIdUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.domain.Product;
import nl.fontys.s3.daclothes.persistence.entity.*;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class GetProductByIdUseCaseImplTest {

    @Test
    void getProductById () {
        ProductRepository productRepository = mock(ProductRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        AccessToken accessToken = mock(AccessToken.class);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("Rodrigo")
                .email("email@email.com")
                .password("Password123").build();
        UserTypeEntity userTypeEntity = UserTypeEntity
                .builder()
                .id(1L)
                .user_type(USER_TYPE.SELLER)
                .user_id(userEntity).build();
        userEntity.setType(Set.of(userTypeEntity));
        CartEntity cartEntity = CartEntity.builder()
                .cartProductList(Collections.emptyList())
                .totalPrice(0)
                .id(1L).build();
        userEntity.setCart(cartEntity);

        OrderEntity orderEntity = OrderEntity.builder()
                .id(1L)
                .order_product_list(Collections.emptyList())
                .created_at(new Date(2020, 12,12))
                .total_price(0)
                .build();
        userEntity.setOrders(List.of(orderEntity));
        long productId = 1L;
        ProductEntity expectedProduct = ProductEntity.builder()
                .id(1L)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .userId(userEntity)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(expectedProduct));


        GetProductByIdUseCase getProductByIdUseCase = new GetProductByIdUseCaseImpl(productRepository);

        Optional<Product> actualProduct = getProductByIdUseCase.getProductById(productId);

        Mockito.verify(productRepository, Mockito.times(1)).findById(productId);

        assertTrue(actualProduct.isPresent());
        assertNotNull(actualProduct.get());
        assertEquals(expectedProduct.getId(), actualProduct.get().getId());
        assertEquals(expectedProduct.getName(), actualProduct.get().getName());

    }
}