package toy.ojm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.ojm.entity.RestaurantEntity;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    // 별도의 메소드 정의 없이 JpaRepository에서 제공하는 기본적인 CRUD 기능을 사용할 수 있습니다.
}