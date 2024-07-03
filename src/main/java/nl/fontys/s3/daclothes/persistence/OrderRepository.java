package nl.fontys.s3.daclothes.persistence;

import nl.fontys.s3.daclothes.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
