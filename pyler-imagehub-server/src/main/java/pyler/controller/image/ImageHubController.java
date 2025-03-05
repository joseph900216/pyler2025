package pyler.controller.image;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import pyler.contents.RequestMap;
import pyler.domain.dto.ImageDTO;
import pyler.domain.dto.ResEntity;
import pyler.domain.entity.CategoryEntity;
import pyler.enums.CategoryEnum;
import pyler.enums.ErrorCode;
import pyler.service.image.ImageHubService;
import pyler.service.image.ImageService;
import pyler.exception.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMap.api + RequestMap.v1 + RequestMap.image)
public class ImageHubController {

    private final ImageHubService imageHubService;

    /***
     * 이미지 등록
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postImageUpload(@Valid @RequestParam("file")MultipartFile file, @RequestPart(value = "request", required = false)ImageDTO.Request request) throws IOException {

        if (request == null) {
            request = new ImageDTO.Request();
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            throw new ServiceException(ErrorCode.INVALID_FILE_TYPE);
        }

        Long userId = getUserId();

        ImageDTO.Response response = imageHubService.postImageUpload(file, request, userId);
        return ResEntity.success(response);
    }

    /***
     * 이미지 조회(미삭제)
     * @return
     * @throws IOException
     */
    @GetMapping
    public ResponseEntity<?> getImageList() throws IOException {
        Long userId = getUserId();
        List<ImageDTO.Response> responseList = imageHubService.getImageList(userId);

        return ResEntity.success(responseList);
    }

    /**
     * 이미지 상세 조회
     * @param imageId
     * @return
     * @throws IOException
     */
    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImageInfo(@Valid @PathVariable Long imageId) throws IOException {
        Long userId = getUserId();
        ImageDTO.Response response = imageHubService.getImageInfo(imageId, userId);
        return ResEntity.success(response);
    }

    /**
     * 이미지 삭제
     * @param imageId
     * @return
     */
    @PutMapping("/{imageId}")
    public ResponseEntity<?> putImageDel(@Valid @PathVariable Long imageId) {
        Long userId = getUserId();
        ImageDTO.delRes delRes =  imageHubService.putImageDel(imageId, userId);
        return ResEntity.success(delRes);
    }

    /**
     * 이미지에 카테고리 추가
     * @param imageId
     * @param categoryEnum
     * @return
     */
    @PostMapping("/{imageId}/category/add")
    public ResponseEntity<?> addCategoryToImage(@Valid @PathVariable Long imageId, @RequestParam CategoryEnum categoryEnum) {
        Long userId = getUserId();

        ImageDTO.Response response = imageHubService.addCategoryToImage(imageId, categoryEnum, userId);
        return ResEntity.success(response);
    }

    /**
     * 카테고리 기준 이미지 조회
     * @param category
     * @return
     */
    @GetMapping("/image/category")
    public ResponseEntity<?> getImageByCategory(@Valid @RequestParam CategoryEnum category) {
        Long userId = getUserId();
        List<ImageDTO.Response> responseList = imageHubService.getImageByCategory(userId, category);
        return ResEntity.success(responseList);
    }

    /**
     * 카테고리 삭제
     * @param imageId
     * @param category
     * @return
     */
    @PutMapping("/{imageId}/category/remove")
    public ResponseEntity<?> removeCategory(@Valid @PathVariable Long imageId, @RequestParam CategoryEnum category) {
        Long userId = getUserId();
        ImageDTO.Response response = imageHubService.removeCategory(imageId, category, userId);
        return ResEntity.success(response);
    }


    private Long getUserId() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 인터셉터에서 설정한 속성 이름으로 값 가져오기
        Object userIdAttribute = request.getAttribute("userId");

        if (userIdAttribute == null) {
            throw new AuthException(ErrorCode.UNAUTHORIZED, "인증된 사용자 정보를 찾을 수 없습니다");
        }

        return (Long) userIdAttribute;// HttpServletRequest에서 속성으로 저장된 사용자 ID 가져오기

    }

    private Boolean getUserMaster() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 인터셉터에서 설정한 속성 이름으로 값 가져오기
        Object isMasterAttribute = request.getAttribute("isMaster");

        if (isMasterAttribute == null) {
            throw new AuthException(ErrorCode.UNAUTHORIZED, "인증된 사용자 정보를 찾을 수 없습니다");
        }

        return (Boolean) isMasterAttribute;
    }
}
