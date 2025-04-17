package toy.ojm.domain.repository;

import toy.ojm.domain.entity.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<FoodCategory, Long> {

}
