package toy.ojm.domain.service;

import org.springframework.data.jpa.domain.Specification;
import toy.ojm.domain.entity.Restaurant;

public class RestaurantSpecifications {

    public static Specification<Restaurant> categoryEquals(String category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"),category);
    }
}
