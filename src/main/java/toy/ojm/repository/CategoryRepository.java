package toy.ojm.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import toy.ojm.entity.FoodCategory;


public interface CategoryRepository extends JpaRepository<FoodCategory, Long>{

    Optional<FoodCategory> findByTopByOrderByIdDesc();


}
