package nl.fontys.s3.daclothes.business.impl.product;

import nl.fontys.s3.daclothes.business.product.GetProductsUseCase;
import nl.fontys.s3.daclothes.domain.GetProductsResponse;
import nl.fontys.s3.daclothes.domain.Product;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.persistence.entity.*;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@DataJpaTest
class GetProductsUseCaseImplTest {

    @Test
    void getAllProductsWithMockito(){
        ProductRepository productRepository = mock(ProductRepository.class);
        UserRepository userRepository = mock(UserRepository.class);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("Rodrigo")
                .email("email@email.com")
                .password("Password123").build();
        UserTypeEntity userTypeEntity = UserTypeEntity
                .builder()
                .id(1L)
                .user_type(USER_TYPE.BUYER)
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

        List<ProductEntity> products = List.of(
                ProductEntity.builder()
                        .id(1L)
                        .brand("Nike")
                        .description("Beautiful blue Sunglasses")
                        .name("Nike sunglasses")
                        .price(100)
                        .size("Normal")
                        .category("Accessories")
                        .productCondition("Good")
                        .userId(userEntity)
                        .build(),
                ProductEntity.builder()
                        .id(2L)
                        .brand("Nike")
                        .category("Shoes")
                        .productCondition("Brand New")
                        .description("Really nice shoes. I don't use them anymore.")
                        .price(23.49)
                        .name("Airforce Max")
                        .size("46")
                        .userId(userEntity)
                        .build()
        );

        when(productRepository.findAll()).thenReturn(products);
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);

        GetProductsUseCase getProductsUseCase = new GetProductsUseCaseImpl(productRepository);
        GetProductsResponse response = getProductsUseCase.getProducts();

        assertEquals(products.size(), response.getProducts().size());

        for (int i = 0; i < products.size(); i++){
            ProductEntity productEntity = products.get(i);
            Product product = response.getProducts().get(i);

            assertEquals(productEntity.getId(), product.getId());
            assertEquals(productEntity.getProductCondition(), product.getProductCondition());
        }

    }
}