package pyler.service.category;

import pyler.domain.dto.CategoryDTO;
import pyler.domain.entity.CategoryEntity;
import pyler.enums.CategoryEnum;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO.Response> getAllCategory();

    CategoryDTO.Response addCategory(CategoryDTO.Request request);
}
