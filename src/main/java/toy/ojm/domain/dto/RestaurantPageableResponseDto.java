package toy.ojm.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import toy.ojm.domain.entity.Restaurant;

@Getter
@AllArgsConstructor
public class RestaurantPageableResponseDto {
    private List<Restaurant> responseDtoList;
    private int pageSize;
    private int currentPage;
    private int totalPage;
    private int totalElements;
}
