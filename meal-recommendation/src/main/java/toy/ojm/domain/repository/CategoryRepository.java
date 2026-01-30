package toy.ojm.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.ojm.domain.entity.FoodCategory;

public interface CategoryRepository extends JpaRepository<FoodCategory, Long> {}
