package pyler.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import pyler.domain.entity.BaseEntity;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "IMAGE_CATEGORY")
public class ImageCategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMAGE_ID")
    private ImageHubEntity image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private CategoryEntity category;

    @Column(name = "IS_DEL")
    private Boolean isDel = false;

    @Column(name = "CREATOR_ID")
    private Long creatorId;

    @Column(name = "UPDATOR_ID")
    private Long updatorId;

    public ImageCategoryEntity(ImageHubEntity imageHubEntity, CategoryEntity categoryEntity, long creatorId) {
        this.image = imageHubEntity;
        this.category = categoryEntity;
        this.creatorId = creatorId;
        this.updatorId = creatorId;
        this.isDel = false;
    }

    void changeIsDel(boolean isDel) {
        this.isDel = isDel;
    }
}
