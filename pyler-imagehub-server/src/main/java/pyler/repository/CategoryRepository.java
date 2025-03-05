package pyler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pyler.domain.entity.CategoryEntity;
import pyler.enums.CategoryEnum;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByCategoryName(CategoryEnum categoryEnum);
}
