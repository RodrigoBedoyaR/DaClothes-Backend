package nl.fontys.s3.daclothes.business.impl.converters;
import nl.fontys.s3.daclothes.domain.Product;
import nl.fontys.s3.daclothes.persistence.entity.ProductEntity;

public final class ProductConverter {
    private ProductConverter() {
    }

    public static Product convert( ProductEntity productEntity){
        return Product.builder()
                .id(productEntity.getId())
                .brand(productEntity.getBrand())
                .category(productEntity.getCategory())
                .productCondition(productEntity.getProductCondition())
                .description(productEntity.getDescription())
                .name(productEntity.getName())
                .price(productEntity.getPrice())
                .size(productEntity.getSize())
                .username(UserConverter.convert(productEntity.getUserId()))
                .available(productEntity.isAvailable())
                .build();
    }
}
