package nl.fontys.s3.daclothes.persistence;

import nl.fontys.s3.daclothes.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCategoryContainingIgnoreCase( String category);
    List<ProductEntity> findBySizeContainingIgnoreCase(String size);
    List<ProductEntity> findByProductConditionContainingIgnoreCase(String condition);
    List<ProductEntity> findByCategoryAndProductConditionAndSizeContainingIgnoreCase(String category, String condition, String size);
    List<ProductEntity> findByProductConditionAndCategoryContainingIgnoreCase(String condition, String category);
    List<ProductEntity> findByCategoryAndSizeContainingIgnoreCase(String category, String size);
    List<ProductEntity> findByProductConditionAndSizeContainingIgnoreCase(String condition, String size);

    List<ProductEntity> findAllByUserId_Id(long id);

    List<ProductEntity> findAllByNameContainingIgnoreCase(String name);
    List<ProductEntity> findAllByDescriptionContainingIgnoreCase(String description);
    List<ProductEntity> findAllByUserId_NameContainingIgnoreCase(String name);

    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.userId.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<ProductEntity> findAllByNameOrDescriptionOrUserNameContainingIgnoreCase(@Param("query") String query);

}
