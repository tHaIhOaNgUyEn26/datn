package code.repository;

import code.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
  List<Category> findById(long category_id) ;
  List<Category> findByName(String category_name) ;
}
