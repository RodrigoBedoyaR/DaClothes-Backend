package nl.fontys.s3.daclothes.business.cart;

import nl.fontys.s3.daclothes.domain.Product;

import java.util.List;

public interface GetCartContentUseCase {
    List<Product> getCartContent( long userId);
}
