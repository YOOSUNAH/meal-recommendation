package toy.ojm.domain.repository;

import org.springframework.data.jpa.domain.Specification;
import toy.ojm.domain.entity.Restaurant;

public class RestaurantSpecifications {

    public static Specification<Restaurant> categoryEquals(String category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("category"),"%" + category+ "%");
    }

    public static Specification<Restaurant> nameContains(String keyword) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),"%" + keyword + "%"));
    }

}
