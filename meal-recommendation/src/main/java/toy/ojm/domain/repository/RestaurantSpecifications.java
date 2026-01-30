package toy.ojm.domain.repository;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import toy.ojm.domain.entity.Restaurant;

public class RestaurantSpecifications {

  public static Specification<Restaurant> alwaysTrue() {
    return ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
  }

  public static Specification<Restaurant> categoryNotIn(List<String> categories) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.not(root.get("category").in(categories));
  }

  public static Specification<Restaurant> categoryEquals(String category) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
  }

  public static Specification<Restaurant> nameContains(String keyword) {
    return ((root, query, criteriaBuilder) ->
        criteriaBuilder.like(root.get("name"), "%" + keyword + "%"));
  }
}
