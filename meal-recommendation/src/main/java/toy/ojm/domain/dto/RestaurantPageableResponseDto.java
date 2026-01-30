package toy.ojm.domain.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import toy.ojm.domain.entity.Restaurant;

@Getter
@RequiredArgsConstructor
public class RestaurantPageableResponseDto {
  private final List<Restaurant> responseDtoList;
  private final int pageSize;
  private final int currentPage;
  private final int totalPage;
  private final int totalElements;
}
