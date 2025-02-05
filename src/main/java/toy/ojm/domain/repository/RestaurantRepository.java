package toy.ojm.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import toy.ojm.domain.entity.Restaurant;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findAllByCategoryIn(List<String> categories);

    List<Restaurant> findAllByManagementNumberIn(@Param("numbers") List<String> managementNumbers);

}

