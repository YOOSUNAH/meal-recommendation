package toy.ojm.domain.repository;

import toy.ojm.domain.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findAllByCategoryIn(List<String> categories);

    List<Restaurant> findAllByManagementNumberIn(List<String> managementNumbers);

}

