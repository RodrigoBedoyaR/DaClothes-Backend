package nl.fontys.s3.daclothes.business.impl.converters;

import nl.fontys.s3.daclothes.domain.Cart;
import nl.fontys.s3.daclothes.persistence.entity.CartEntity;

import java.util.List;

public final class CartConverter {
    private CartConverter() {
    }
    public static Cart convert( CartEntity entity ){
        return Cart.builder()
                .id(entity.getId())
                .cartItemList(List.copyOf(entity.getCartProductList().stream().map(ProductConverter::convert).toList()))
                .totalPrice(entity.getTotalPrice())
                .build();
    }
}
