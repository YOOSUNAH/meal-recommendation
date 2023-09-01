package toy.ojm.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import toy.ojm.domain.Restaurant;
import toy.ojm.domain.RestaurantRepository;
import toy.ojm.domain.RestaurantRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

        private final RestaurantRepository restaurantRepository;
        private boolean selectAll; // "모두 선택하기" 옵션을 선택했는지 여부를 저장하는 변수

        @Autowired
        public RestaurantService(RestaurantRepository restaurantRepository) {
            this.restaurantRepository = restaurantRepository;
        }

        public List<Restaurant> retrieveRecommendedRestaurants(RestaurantRequest request) {
            if (selectAll) {
                // "모두 선택하기"를 선택한 경우, 데이터베이스와 가짜 데이터를 모두 가져와서 합침
                List<Restaurant> dbRecommendations = retrieveFromDatabase(request);
                List<Restaurant> fakeRecommendations = generateFakeRecommendations(request);
                dbRecommendations.addAll(fakeRecommendations);
                return dbRecommendations;
            } else {
                // "모두 선택하기"를 선택하지 않은 경우, 데이터베이스 또는 가짜 데이터 중 하나를 선택
                return retrieveFromDatabase(request);
            }
        }

        private List<Restaurant> retrieveFromDatabase(RestaurantRequest request) {
            // 데이터베이스에서 음식점을 가져오는 로직을 구현
            // restaurantRepository를 사용하여 데이터베이스에서 데이터를 조회하고 반환
            List<Restaurant> recommendedRestaurants = restaurantRepository.findByMenu(request.getMenu());
            return recommendedRestaurants;
        }

        private List<Restaurant> generateFakeRecommendations(RestaurantRequest request) {
            // 가짜 음식점 데이터를 생성하는 로직을 구현
            List<Restaurant> recommendedRestaurants = new ArrayList<>();
            recommendedRestaurants.add(new Restaurant(
                    "KOREAN",
                    "뱅뱅막국수 베이비",
                    "11231232.2131233",
                    "11231232.2131233",
                    "들기름 막국수 베이비"));
            // 추가적인 가짜 음식점 데이터 생성
            return recommendedRestaurants;
        }

}
