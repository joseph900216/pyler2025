package pyler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pyler.domain.entity.ImageHubEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageHubEntity, Long> {
}
