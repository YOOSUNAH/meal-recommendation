package toy.ojm.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.ojm.domain.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}
