package toy.ojm.domain.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestaurantRequestDto {

    private final double currentLat;
    private final double currentLon;
    private final List<String> selectedCategories;
}
