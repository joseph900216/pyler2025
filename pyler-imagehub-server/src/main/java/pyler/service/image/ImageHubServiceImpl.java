package pyler.service.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pyler.domain.dto.ImageDTO;
import pyler.domain.entity.CategoryEntity;
import pyler.domain.entity.ImageCategoryEntity;
import pyler.domain.entity.ImageHubEntity;
import pyler.enums.CategoryEnum;
import pyler.enums.ErrorCode;
import pyler.exception.ServiceException;
import pyler.repository.CategoryRepository;
import pyler.repository.ImageRepository;

import java.awt.font.ImageGraphicAttribute;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageHubServiceImpl implements ImageHubService {


    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    /**
     * 이미지 업로드
     * @param multipartFile
     * @param request
     * @param userId
     * @return
     * @throws IOException
     */
    @Transactional
    @Override
    public ImageDTO.Response postImageUpload(MultipartFile multipartFile, ImageDTO.Request request, Long userId) throws IOException {
        try {
            String fileName = imageService.imageUpload(multipartFile);

            String fileExt = imageService.imageExt(multipartFile);

            String thumbnailName = imageService.createThumbnail(fileName);

            Set<CategoryEntity> categoryEntities = new HashSet<>();
            if (request.getCategory() != null && !request.getCategory().isEmpty()) {
                request.getCategory().forEach(categoryType -> {
                    CategoryEntity categoryEntity = categoryRepository.findByCategoryName(categoryType)
                            .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "카테고리를 찾을 수 없습니다. " + categoryType));
                });
            }

            ImageHubEntity imageHubEntity = ImageHubEntity.builder()
                    .imageName(fileName)
                    .imageOriginName(multipartFile.getOriginalFilename())
                    .imagePath("/uploads/" + fileName)
                    .thumbnailPath("/thumbnail/" + thumbnailName)
                    .imageSize(multipartFile.getSize())
                    .imageContentType(multipartFile.getContentType())
                    .imageDescription(request.getDescription())
                    .imageExt(fileExt)
                    .isDel(false)
                    .creatorId(userId)
                    .updatorId(userId)
                    .build();

            ImageHubEntity saveImage = imageRepository.save(imageHubEntity);


            return convertToResponseDTO(saveImage);

        } catch (IOException e) {
            log.error("이미 업로드 오류: {}", e.getMessage());
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "이미지 업로드 오류");
        }
    }

    /**
     * 이미지 목록 조회
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<ImageDTO.Response> getImageList(Long userId) {

        //삭제 된 이미지 제외
        List<ImageHubEntity> imageHubEntities = imageRepository.findByCreatorIdAndIsDel(userId, false);
        return imageHubEntities.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

    }

    /***
     * 이미지 정보 조회
     * @param imageId
     * @param userId
     * @return
     * @throws IOException
     */
    @Transactional(readOnly = true)
    @Override
    public ImageDTO.Response getImageInfo(Long imageId, Long userId) throws IOException {
        ImageHubEntity imageHubEntity = imageRepository.findByIdAndCreatorIdAndIsDel(imageId, userId, false)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "이미지를 찾을 수 없습니다."));

        return convertToResponseDTO(imageHubEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ImageDTO.Response> getImageByCategory(Long userId, CategoryEnum categoryEnum) {
        List<ImageHubEntity> imageHubEntities = imageRepository.findByCreatorIdAndImageCategoryEntities_Category_CategoryNameAndIsDel(userId, categoryEnum, false);
        if(imageHubEntities.isEmpty())
            throw new ServiceException(ErrorCode.IMAGE_IS_NOT_EXIST);

        return imageHubEntities.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /***
     * 이미지 삭제
     * @param imageId
     * @param userId
     */
    @Transactional
    @Override
    public ImageDTO.delRes putImageDel(Long imageId, Long userId) {
        ImageHubEntity imageHubEntity = imageRepository.findByIdAndCreatorIdAndIsDel(imageId, userId, false)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "이미지를 찾을 수 없습니다."));

        try {
            imageService.imageDelete(imageHubEntity.getImageName());

            imageHubEntity.changeIsDel(imageId);

            ImageHubEntity imageDel = imageRepository.save(imageHubEntity);

            return ImageDTO.delRes.builder().imageId(imageDel.getId()).build();

        } catch (IOException e) {
            log.error("이미지 삭제 에러", e.getMessage());
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "이미지 삭제 에러");
        }
    }

    /**
     * 카테고리 추가
     * @param imageId
     * @param categoryEnum
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public ImageDTO.Response addCategoryToImage(Long imageId, CategoryEnum categoryEnum, Long userId) {
        ImageHubEntity imageHubEntity = imageRepository.findByIdAndCreatorIdAndIsDel(imageId, userId, false)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "이미지를 찾을 수 없습니다."));

        CategoryEntity categoryEntity = categoryRepository.findByCategoryName(categoryEnum)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "카테고리를 찾을 수 없습니다."));

        if(imageHubEntity.getImageCategoryEntities().size() >= 5) {
            throw new ServiceException(ErrorCode.BAD_REQUEST, "이미지는 최대 5개의 카테고리만 가질 수 있습니다.");
        }

        imageHubEntity.addCategory(categoryEntity);
        ImageHubEntity updateImage = imageRepository.save(imageHubEntity);

        return convertToResponseDTO(updateImage);
    }

    /***
     * 카테고리 제거
     * @param imageId
     * @param categoryEnum
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public ImageDTO.Response removeCategory(Long imageId, CategoryEnum categoryEnum, Long userId) {
        ImageHubEntity imageHubEntity = imageRepository.findByIdAndCreatorIdAndIsDel(imageId, userId, false)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "이미지를 찾을 수 없습니다."));

        CategoryEntity categoryEntity = categoryRepository.findByCategoryName(categoryEnum)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "카테고리를 찾을 수 없습니다."));

//        imageHubEntity.removeCategory(categoryEntity);


        ImageHubEntity updateImage = imageRepository.save(imageHubEntity);

        return convertToResponseDTO(updateImage);
    }


    private ImageDTO.Response convertToResponseDTO(ImageHubEntity imageHubEntity) {
        Set<CategoryEnum> categoryEnums = imageHubEntity.getImageCategoryEntities().stream()
                .map(imageCategoryEntity -> imageCategoryEntity.getCategory().getName())
                .collect(Collectors.toSet());

        return ImageDTO.Response.builder()
                .id(imageHubEntity.getId())
                .fileName(imageHubEntity.getImageName())
                .originFileName(imageHubEntity.getImageOriginName())
                .filePath(imageHubEntity.getImagePath())
                .thumbnailPath(imageHubEntity.getThumbnailPath())
                .fileSize(imageHubEntity.getImageSize())
                .fileExt(imageHubEntity.getImageExt())
                .contentType(imageHubEntity.getImageContentType())
                .categoryEnums(categoryEnums)
                .createdAt(imageHubEntity.getCreatedAt())
                .updatedAt(imageHubEntity.getUpdatedAt())
                .build();

    }
}
