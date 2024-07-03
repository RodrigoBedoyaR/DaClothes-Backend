package nl.fontys.s3.daclothes.business.order;

import nl.fontys.s3.daclothes.domain.Order;

import java.util.List;

public interface GetOrdersFromUserUseCase {
    List<Order> getOrdersByUser();
}
