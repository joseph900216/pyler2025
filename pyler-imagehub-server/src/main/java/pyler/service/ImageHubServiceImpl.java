package pyler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pyler.domain.dto.ImageDTO;
import pyler.domain.entity.ImageHubEntity;
import pyler.repository.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageHubServiceImpl implements ImageHubService {


    private final ImageRepository imageRepository;
    private final ImageUploadService imageUploadService;

    @Override
    public ImageDTO postImageUpload(MultipartFile multipartFile, ImageDTO imageDTO, Long userId) throws IOException {

        String fileName = imageUploadService.imageUpload(multipartFile);

        String thumbnailName = imageUploadService.createThumbnail(fileName);

        ImageHubEntity imageHubEntity = ImageHubEntity.builder()
                .imageName(fileName)
                .imageOriginName(multipartFile.getOriginalFilename())
                .imagePath("/uploads/" + fileName)
                .thumbnailPath("/thumbnail/" + thumbnailName)
                .imageSize(multipartFile.getSize())
                .imageContentType(multipartFile.getContentType())
                .imageDescription(imageDTO.getDescription())
                .creatorId(userId)
                .updatorId(userId)
                .build();

        ImageHubEntity saveImage = imageRepository.save(imageHubEntity);

        return imageDTO;
    }
}
