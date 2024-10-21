package code.service.admin;

import code.entity.Category;
import code.exception.NotFoundException;
import code.model.request.CreateCategoryRequest;
import code.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
  private CategoryRepository categoryRepository;
  public CategoryService(CategoryRepository categoryRepository){
    this.categoryRepository = categoryRepository;
  }

  public List<Category> getCategories(){
    return this.categoryRepository.findAll();
  }

//  public Category createCategory(CreateCategoryRequest createCategoryRequest){
//    String  categoryName = createCategoryRequest.getName();
//    if(this.categoryRepository.findByName(categoryName) != null){
//      throw new NotFoundException("Không tìm thấy người dùng với id: " );
//    }
//  }
}
