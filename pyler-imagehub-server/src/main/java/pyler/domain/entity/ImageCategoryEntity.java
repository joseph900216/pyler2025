package pyler.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import pyler.domain.entity.BaseEntity;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CATEGORY")
public class ImageCategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IMAGE_ID")
    private Long imageId;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    @Column(name = "IS_DEL")
    private Boolean isDel;

    @Column(name = "CREATOR_ID")
    private Long creatorId;

    @Column(name = "UPDATOR_ID")
    private Long updatorId;
}
