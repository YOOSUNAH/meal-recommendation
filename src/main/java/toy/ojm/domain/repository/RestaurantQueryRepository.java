package toy.ojm.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import toy.ojm.domain.entity.QRestaurant;
import toy.ojm.domain.entity.Restaurant;

@Repository
@RequiredArgsConstructor
public class RestaurantQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Restaurant> findByCategoryAndKeyword(
        Pageable pageable,
        String category,
        String keyword
    ) {
        QRestaurant restaurant = QRestaurant.restaurant;
        BooleanBuilder whereClause = new BooleanBuilder();

        // 카테고리가 기타인 경우
        if ("기타".equals(category)) {
            whereClause.and(restaurant.category.notIn("한식", "중식", "일식", "양식"));
        }
        // 카테고리가 특정 값인 경우
        else if (!category.isEmpty() && !"전체".equals(category)) {
            whereClause.and(restaurant.category.eq(category));
        }

        // 키워드가 있는 경우
        if (!keyword.isEmpty()) {
            whereClause.and(restaurant.name.contains(keyword));
        }

        Long totalSize = jpaQueryFactory
            .select(Wildcard.count)
            .from(restaurant)
            .where(whereClause)
            .fetchOne();

        List<Restaurant> restaurants = jpaQueryFactory
            .select(Projections.fields(Restaurant.class,
                restaurant.name,
                restaurant.category,
                restaurant.address,
                restaurant.number))
            .from(restaurant)
            .where(whereClause)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return PageableExecutionUtils.getPage(restaurants, pageable, () -> totalSize);
    }
//
//    public Page<Restaurant> getRestaurantListWithPage(  Pageable pageable, long id){
//        QRestaurant restaurant = QRestaurant.restaurant;
//
//        List<Restaurant> restaurants = jpaQueryFactory
//                .select(Projections.fields(Restaurant.class,
//                        restaurant.name,
//                        restaurant.category,
//                        restaurant.address,
//                        restaurant.number))
//                .from(restaurant)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//    }

}

