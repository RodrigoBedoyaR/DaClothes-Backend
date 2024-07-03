package nl.fontys.s3.daclothes.business.impl.product;

import nl.fontys.s3.daclothes.business.impl.converters.ProductConverter;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.domain.Product;
import nl.fontys.s3.daclothes.domain.User;
import nl.fontys.s3.daclothes.persistence.entity.*;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductConverterTest {

    @Test
    void productConverter_convertsEntity_toDomainObject(){
        UserEntity user = new UserEntity();
        user.setEmail("r.bedoya@email.com");
        user.setId(1L);
        user.setName("Name");
        user.setPassword("pw");
        UserTypeEntity userTypeEntity = new UserTypeEntity();
        userTypeEntity.setUser_type(USER_TYPE.BUYER);
        userTypeEntity.setUser_id(user);
        userTypeEntity.setId(1L);
        user.setType(Set.of(userTypeEntity));
        CartEntity cartEntity = CartEntity.builder()
                .cartProductList(Collections.emptyList())
                .totalPrice(0)
                .id(1L).build();
        user.setCart(cartEntity);

        OrderEntity orderEntity = OrderEntity.builder()
                .id(1L)
                .order_product_list(Collections.emptyList())
                .created_at(new Date(2020, 12,12))
                .total_price(0)
                .build();
        user.setOrders(List.of(orderEntity));

        ProductEntity productEntity = new ProductEntity();
        productEntity.setBrand("Brand");
        productEntity.setCategory("Category");
        productEntity.setDescription("Description");
        productEntity.setId(1L);
        productEntity.setName("Name");
        productEntity.setPrice(10.0d);
        productEntity.setProductCondition("Product Condition");
        productEntity.setSize("Size");
        productEntity.setUserId(user);
        Product actualConvertResult = ProductConverter.convert(productEntity);
        assertEquals("Brand", actualConvertResult.getBrand());
        assertEquals("Category", actualConvertResult.getCategory());
        assertEquals("Name", actualConvertResult.getName());
        User username = actualConvertResult.getUsername();
        assertEquals("Name", username.getName());
        assertEquals("Product Condition", actualConvertResult.getProductCondition());
        assertEquals("Size", actualConvertResult.getSize());
        assertEquals("Description", actualConvertResult.getDescription());
        assertEquals("pw", username.getPassword());
        assertEquals("r.bedoya@email.com", username.getEmail());
        assertEquals(10.0d, actualConvertResult.getPrice());
        assertEquals(1L, actualConvertResult.getId());
        assertEquals(1L, username.getId());
        assertEquals(1, username.getType().size());
    }

}