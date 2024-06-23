package toy.ojm.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import toy.ojm.domain.entity.Restaurant;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(value = """
//        select * 
//        from restaurants 
//        where 1 = 1
//        and 
        """, nativeQuery = true)
    List<Restaurant> findNearByRestaurant(Double longitude, Double latitude);

    List<Restaurant> findAllByCategory(String category);
}
