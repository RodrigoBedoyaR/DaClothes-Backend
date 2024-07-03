package nl.fontys.s3.daclothes.business.impl.converters;

import nl.fontys.s3.daclothes.domain.Order;
import nl.fontys.s3.daclothes.persistence.entity.OrderEntity;

public final class OrderConverter {
    private OrderConverter() {
    }
    public static Order convert( OrderEntity entity ){
        return Order.builder()
                .id(entity.getId())
                .orderProductList(entity.getOrder_product_list().stream().map(ProductConverter::convert).toList())
                .totalPrice(entity.getTotal_price())
                .createdAt(entity.getCreated_at())
                .build();
    }
}
