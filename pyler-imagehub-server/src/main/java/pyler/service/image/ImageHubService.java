package pyler.service.image;

import org.springframework.web.multipart.MultipartFile;
import pyler.domain.dto.ImageDTO;
import pyler.enums.CategoryEnum;

import java.io.IOException;
import java.util.List;

public interface ImageHubService {

    ImageDTO.Response postImageUpload(MultipartFile multipartFile, ImageDTO.Request request, Long userId) throws IOException;

    List<ImageDTO.Response> getImageList(Long userId);

    ImageDTO.Response getImageInfo(Long imageId, Long userId) throws IOException;

    ImageDTO.delRes putImageDel(Long imageId, Long userId);

    ImageDTO.Response addCategoryToImage(Long imageId, CategoryEnum categoryEnum, Long userId);

    ImageDTO.Response removeCategory(Long imageId, CategoryEnum categoryEnum, Long userId);

    List<ImageDTO.Response> getImageByCategory(Long userId, CategoryEnum categoryEnum);

}
