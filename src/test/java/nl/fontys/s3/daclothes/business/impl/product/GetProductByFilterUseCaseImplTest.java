package nl.fontys.s3.daclothes.business.impl.product;

import nl.fontys.s3.daclothes.business.impl.converters.UserConverter;
import nl.fontys.s3.daclothes.business.product.GetProductsByFilteringUseCase;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.GetProductsResponse;
import nl.fontys.s3.daclothes.domain.Product;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.persistence.entity.*;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetProductByFilterUseCaseImplTest {


    @Test
    void getProductsByFilter_categoryAndSize () {
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
        List<ProductEntity> products = List.of(
                ProductEntity.builder()
                        .id(1)
                        .brand("Nike")
                        .category("Shoes")
                        .productCondition("Brand New")
                        .description("Really nice shoes. I don't use them anymore.")
                        .price(23.49)
                        .name("Airforce Max")
                        .size("46")
                        .userId(userEntity)
                        .build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(productRepository.findByCategoryAndSizeContainingIgnoreCase("Shoes", "46")).thenReturn(products);
        when(accessToken.getRoles()).thenReturn(Set.of(USER_TYPE.SELLER.toString()));

        GetProductsByFilteringUseCase getProductByFilterUseCase = new GetProductsByFilteringUseCaseImpl(productRepository);
        GetProductsResponse result = getProductByFilterUseCase.getProductsByFilters("Shoes", "46", null);
        Product expectedProduct = Product.builder()
                .id(1)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .username(UserConverter.convert(userEntity))
                .build();

        for (Product product : result.getProducts()){
            assertEquals(expectedProduct.getName(), product.getName());
            assertEquals(expectedProduct.getId(), product.getId());
            assertEquals(expectedProduct.getProductCondition(), product.getProductCondition());
        }
        assertEquals(1, result.getProducts().size());
    }
    @Test
    void getProductsByFilter_condition () {
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
        List<ProductEntity> products = List.of(
                ProductEntity.builder()
                        .id(1)
                        .brand("Nike")
                        .category("Shoes")
                        .productCondition("Brand New")
                        .description("Really nice shoes. I don't use them anymore.")
                        .price(23.49)
                        .name("Airforce Max")
                        .size("46")
                        .userId(userEntity)
                        .build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(productRepository.findByProductConditionContainingIgnoreCase( "Brand New")).thenReturn(products);
        when(accessToken.getRoles()).thenReturn(Set.of(USER_TYPE.SELLER.toString()));

        GetProductsByFilteringUseCase getProductByFilterUseCase = new GetProductsByFilteringUseCaseImpl(productRepository);
        GetProductsResponse result = getProductByFilterUseCase.getProductsByFilters(null, null, "Brand New");
        Product expectedProduct = Product.builder()
                .id(1)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .username(UserConverter.convert(userEntity))
                .build();

        for (Product product : result.getProducts()){
            assertEquals(expectedProduct.getName(), product.getName());
            assertEquals(expectedProduct.getId(), product.getId());
            assertEquals(expectedProduct.getProductCondition(), product.getProductCondition());
        }
        assertEquals(1, result.getProducts().size());
    }

    @Test
    void getProductsByFilter_categorySizeAndCondition () {
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
        List<ProductEntity> products = List.of(
                ProductEntity.builder()
                        .id(1)
                        .brand("Nike")
                        .category("Shoes")
                        .productCondition("Brand New")
                        .description("Really nice shoes. I don't use them anymore.")
                        .price(23.49)
                        .name("Airforce Max")
                        .size("46")
                        .userId(userEntity)
                        .build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(productRepository.findByCategoryAndProductConditionAndSizeContainingIgnoreCase( "Shoes", "Brand New", "46")).thenReturn(products);
        when(accessToken.getRoles()).thenReturn(Set.of(USER_TYPE.SELLER.toString()));

        GetProductsByFilteringUseCase getProductByFilterUseCase = new GetProductsByFilteringUseCaseImpl(productRepository);
        GetProductsResponse result = getProductByFilterUseCase.getProductsByFilters("Shoes", "46", "Brand New");
        Product expectedProduct = Product.builder()
                .id(1)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .username(UserConverter.convert(userEntity))
                .build();

        for (Product product : result.getProducts()){
            assertEquals(expectedProduct.getName(), product.getName());
            assertEquals(expectedProduct.getId(), product.getId());
            assertEquals(expectedProduct.getProductCondition(), product.getProductCondition());
        }
        assertEquals(1, result.getProducts().size());
    }

    @Test
    void getProductsByFilter_conditionAndSize () {
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
        List<ProductEntity> products = List.of(
                ProductEntity.builder()
                        .id(1)
                        .brand("Nike")
                        .category("Shoes")
                        .productCondition("Brand New")
                        .description("Really nice shoes. I don't use them anymore.")
                        .price(23.49)
                        .name("Airforce Max")
                        .size("46")
                        .userId(userEntity)
                        .build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(productRepository.findByProductConditionAndSizeContainingIgnoreCase( "Brand New", "46")).thenReturn(products);
        when(accessToken.getRoles()).thenReturn(Set.of(USER_TYPE.SELLER.toString()));

        GetProductsByFilteringUseCase getProductByFilterUseCase = new GetProductsByFilteringUseCaseImpl(productRepository);
        GetProductsResponse result = getProductByFilterUseCase.getProductsByFilters(null, "46", "Brand New");
        Product expectedProduct = Product.builder()
                .id(1)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .username(UserConverter.convert(userEntity))
                .build();

        for (Product product : result.getProducts()){
            assertEquals(expectedProduct.getName(), product.getName());
            assertEquals(expectedProduct.getId(), product.getId());
            assertEquals(expectedProduct.getProductCondition(), product.getProductCondition());
        }
        assertEquals(1, result.getProducts().size());
    }

    @Test
    void getProductsByFilter_categoryAndCondition () {
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
        List<ProductEntity> products = List.of(
                ProductEntity.builder()
                        .id(1)
                        .brand("Nike")
                        .category("Shoes")
                        .productCondition("Brand New")
                        .description("Really nice shoes. I don't use them anymore.")
                        .price(23.49)
                        .name("Airforce Max")
                        .size("46")
                        .userId(userEntity)
                        .build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(productRepository.findByProductConditionAndCategoryContainingIgnoreCase( "Brand New", "Shoes")).thenReturn(products);
        when(accessToken.getRoles()).thenReturn(Set.of(USER_TYPE.SELLER.toString()));

        GetProductsByFilteringUseCase getProductByFilterUseCase = new GetProductsByFilteringUseCaseImpl(productRepository);
        GetProductsResponse result = getProductByFilterUseCase.getProductsByFilters("Shoes", null, "Brand New");
        Product expectedProduct = Product.builder()
                .id(1)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .username(UserConverter.convert(userEntity))
                .build();

        for (Product product : result.getProducts()){
            assertEquals(expectedProduct.getName(), product.getName());
            assertEquals(expectedProduct.getId(), product.getId());
            assertEquals(expectedProduct.getProductCondition(), product.getProductCondition());
        }
        assertEquals(1, result.getProducts().size());
    }

    @Test
    void getProductsByFilter_category () {
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
        List<ProductEntity> products = List.of(
                ProductEntity.builder()
                        .id(1)
                        .brand("Nike")
                        .category("Shoes")
                        .productCondition("Brand New")
                        .description("Really nice shoes. I don't use them anymore.")
                        .price(23.49)
                        .name("Airforce Max")
                        .size("46")
                        .userId(userEntity)
                        .build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(productRepository.findByCategoryContainingIgnoreCase( "Shoes")).thenReturn(products);
        when(accessToken.getRoles()).thenReturn(Set.of(USER_TYPE.SELLER.toString()));

        GetProductsByFilteringUseCase getProductByFilterUseCase = new GetProductsByFilteringUseCaseImpl(productRepository);
        GetProductsResponse result = getProductByFilterUseCase.getProductsByFilters("Shoes", null, null);
        Product expectedProduct = Product.builder()
                .id(1)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .username(UserConverter.convert(userEntity))
                .build();

        for (Product product : result.getProducts()){
            assertEquals(expectedProduct.getName(), product.getName());
            assertEquals(expectedProduct.getId(), product.getId());
            assertEquals(expectedProduct.getProductCondition(), product.getProductCondition());
        }
        assertEquals(1, result.getProducts().size());
    }

    @Test
    void getProductsByFilter_size () {
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
        List<ProductEntity> products = List.of(
                ProductEntity.builder()
                        .id(1)
                        .brand("Nike")
                        .category("Shoes")
                        .productCondition("Brand New")
                        .description("Really nice shoes. I don't use them anymore.")
                        .price(23.49)
                        .name("Airforce Max")
                        .size("46")
                        .userId(userEntity)
                        .build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(productRepository.findBySizeContainingIgnoreCase( "46")).thenReturn(products);
        when(accessToken.getRoles()).thenReturn(Set.of(USER_TYPE.SELLER.toString()));

        GetProductsByFilteringUseCase getProductByFilterUseCase = new GetProductsByFilteringUseCaseImpl(productRepository);
        GetProductsResponse result = getProductByFilterUseCase.getProductsByFilters(null, "46", null);
        Product expectedProduct = Product.builder()
                .id(1)
                .brand("Nike")
                .category("Shoes")
                .productCondition("Brand New")
                .description("Really nice shoes. I don't use them anymore.")
                .price(23.49)
                .name("Airforce Max")
                .size("46")
                .username(UserConverter.convert(userEntity))
                .build();

        for (Product product : result.getProducts()){
            assertEquals(expectedProduct.getName(), product.getName());
            assertEquals(expectedProduct.getId(), product.getId());
            assertEquals(expectedProduct.getProductCondition(), product.getProductCondition());
        }
        assertEquals(1, result.getProducts().size());
    }
}
