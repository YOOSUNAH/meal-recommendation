package toy.ojm.domain.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import toy.ojm.domain.entity.QRestaurant;
import toy.ojm.domain.entity.Restaurant;

@Repository
@RequiredArgsConstructor
public class RestaurantQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    public List<Restaurant> findAllByCategory(String category) {
        QRestaurant restaurant = QRestaurant.restaurant;

        return jpaQueryFactory
            .selectFrom(restaurant)
            .where(restaurant.category.contains(category))
            .fetch();
    }

}
