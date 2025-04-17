package toy.ojm.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.repository.RestaurantRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    // api 용
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
}
