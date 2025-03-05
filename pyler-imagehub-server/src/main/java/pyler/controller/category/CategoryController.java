package pyler.controller.category;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pyler.contents.RequestMap;
import pyler.domain.dto.CategoryDTO;
import pyler.domain.dto.ResEntity;
import pyler.enums.CategoryEnum;
import pyler.enums.ErrorCode;
import pyler.exception.AuthException;
import pyler.exception.ServiceException;
import pyler.service.category.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMap.api + RequestMap.v1 + RequestMap.category)
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 목록 조회
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        List<CategoryDTO.Response> responseList = categoryService.getAllCategory();
        return ResEntity.success(responseList);
    }

    /**
     * 카테고리 등록
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDTO.Request request) {
        try {
            boolean isMaster = getUserMaster();
            if (!isMaster)
                throw new ServiceException(ErrorCode.BAD_REQUEST, "관리자만 접근 가능 합니다.");
            CategoryDTO.Response Response = categoryService.addCategory(request);
            return ResEntity.success(Response);
        } catch (Exception e) {
            log.error("카테고리 등록 에러", e.getMessage());
            throw new ServiceException(ErrorCode.CATGORY_IS_NOT_MATCHING);
        }
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
