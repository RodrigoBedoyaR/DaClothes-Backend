package nl.fontys.s3.daclothes.persistence;

import nl.fontys.s3.daclothes.persistence.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    void deleteByIdAndCartProductList_Id ( long id, long productId );

}