package nl.fontys.s3.daclothes.persistence;

import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
     UserEntity findByEmail(String email);
     boolean existsByEmail(String email);
}
