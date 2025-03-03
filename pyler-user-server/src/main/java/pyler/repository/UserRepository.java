package pyler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pyler.domain.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserEmailAndIsDel(String userEmail, boolean isDel);
    Optional<UserEntity> findByUserEmailAndPassWordAndIsDel(String userEmail, String passWord, boolean isDel);
}
