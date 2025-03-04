package pyler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pyler.domain.entity.UserUserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserUserEntity, Long> {

    Optional<UserUserEntity> findByUserEmailAndIsDel(String userEmail, boolean isDel);
    Optional<UserUserEntity> findByUserEmailAndPassWordAndIsDel(String userEmail, String passWord, boolean isDel);
}
