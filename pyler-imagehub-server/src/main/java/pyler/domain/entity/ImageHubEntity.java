package pyler.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import pyler.domain.entity.BaseEntity;
import pyler.enums.CategoryEnum;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "IMAGE_IMAGEHUB")
public class ImageHubEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IMAGE_ORIGIN_NAME")
    private String imageOriginName;

    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Column(name = "IMAGE_EXT")
    private String imageExt;

    @Column(name = "IMAGE_SIZE")
    private long imageSize;

    @Column(name = "IMAGE_CONTENT_TYPE")
    private String imageContentType;

    @Column(name = "IMAGE_DESCRIPTION")
    private String imageDescription;

    @Column(name = "IMAGE_PATH")
    private String imagePath;

    @Column(name = "THUMBNAIL_PATH")
    private String thumbnailPath;

    @Column(name = "IS_DEL")
    private Boolean isDel;

    @Column(name = "CREATOR_ID")
    private long creatorId;

    @Column(name = "UPDATOR_ID")
    private long updatorId;



}
