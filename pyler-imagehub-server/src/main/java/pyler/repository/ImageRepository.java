package pyler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pyler.domain.entity.ImageHubEntity;
import pyler.enums.CategoryEnum;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageHubEntity, Long> {

    List<ImageHubEntity> findByCreatorIdAndIsDel(Long userId, Boolean isDel);

    Optional<ImageHubEntity> findByIdAndCreatorIdAndIsDel(Long imageId, Long userId, Boolean isDel);

    List<ImageHubEntity> findByCreatorIdAndImageCategoryEntities_Category_CategoryNameAndIsDel(Long userId, CategoryEnum categoryName, boolean isDel);

}
