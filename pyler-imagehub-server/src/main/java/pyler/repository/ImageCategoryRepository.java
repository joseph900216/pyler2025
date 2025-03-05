package pyler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pyler.domain.entity.CategoryEntity;
import pyler.domain.entity.ImageCategoryEntity;
import pyler.domain.entity.ImageHubEntity;

import java.util.Optional;

@Repository
public interface ImageCategoryRepository extends JpaRepository<ImageCategoryEntity, Long> {


}
