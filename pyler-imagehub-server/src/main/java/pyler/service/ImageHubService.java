package pyler.service;

import org.springframework.web.multipart.MultipartFile;
import pyler.domain.dto.ImageDTO;

import java.io.IOException;

public interface ImageHubService {

    ImageDTO postImageUpload(MultipartFile multipartFile, ImageDTO imageDTO, Long userId) throws IOException;

}
