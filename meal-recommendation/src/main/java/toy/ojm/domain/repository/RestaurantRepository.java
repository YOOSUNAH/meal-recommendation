package toy.ojm.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import toy.ojm.domain.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

  List<Restaurant> findAllByCategoryIn(List<String> categories);

  List<Restaurant> findAllByManagementNumberIn(List<String> managementNumbers);
}
