package toy.ojm.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import toy.ojm.domain.entity.Restaurant;

@Getter
@AllArgsConstructor
public class RestaurantPageableResponseDto {
     List<Restaurant> responseDtoList;
     int pageSize;
     int currentPage;
     int totalPage;
}
