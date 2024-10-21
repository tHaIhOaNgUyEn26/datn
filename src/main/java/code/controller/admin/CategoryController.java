package code.controller.admin;

import code.entity.Category;
import code.service.admin.CategoryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
public class CategoryController {
  private CategoryService categoryService;

  public CategoryController(CategoryService categoryService){
    this.categoryService = categoryService;
  }

  @GetMapping("/categories")
  public ResponseEntity<List<Category>> getCategories(){
    return ResponseEntity.ok(this.categoryService.getCategories());
  }

  @PostMapping("/categories")
  public ResponseEntity<List<Category>> createCategory(){
    return ResponseEntity.ok(this.categoryService.getCategories());
  }
}
