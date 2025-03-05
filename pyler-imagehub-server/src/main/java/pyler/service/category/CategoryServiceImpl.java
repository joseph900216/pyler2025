package pyler.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pyler.domain.dto.CategoryDTO;
import pyler.domain.entity.CategoryEntity;
import pyler.enums.CategoryEnum;
import pyler.enums.ErrorCode;
import pyler.exception.ServiceException;
import pyler.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDTO.Response> getAllCategory() {
        List<CategoryEntity> categories = categoryRepository.findAll();

        return categories.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 등록
     * @param request
     * @return
     */
    @Transactional
    @Override
    public CategoryDTO.Response addCategory(CategoryDTO.Request request) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findByCategoryName(request.getCategoryEnum());
        if (categoryEntity.isPresent())
            throw new ServiceException(ErrorCode.ALREADY_EXIST_CATEGORY);

        CategoryEntity category = CategoryEntity.builder()
                .categoryName(request.getCategoryEnum())
                .build();

        CategoryEntity saveCategory = categoryRepository.save(category);

        return CategoryDTO.Response.builder()
                .id(saveCategory.getId())
                .categoryEnum(saveCategory.getCategoryName())
                .build();
    }

    private CategoryDTO.Response convertToResponseDto(CategoryEntity category) {
        return CategoryDTO.Response.builder()
                .id(category.getId())
                .categoryEnum(category.getCategoryName())
                .build();
    }
}
