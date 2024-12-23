package toy.ojm.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import toy.ojm.domain.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findAllByCategoryIn(List<String> categories);

    Page<Restaurant> findAll(Specification<Restaurant> specification, Pageable pageable);

    Optional<Restaurant> findByManagementNumber(String managementNumber);
}

