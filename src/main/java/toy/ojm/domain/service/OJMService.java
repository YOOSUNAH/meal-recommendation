package toy.ojm.domain.service;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import toy.ojm.domain.dto.CategoryRequestDto;
import toy.ojm.domain.dto.RestaurantResponseDto;
import toy.ojm.domain.entity.FoodCategory;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.location.TransCoordination;
import toy.ojm.domain.repository.CategoryRepository;
import toy.ojm.domain.repository.RestaurantRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class OJMService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final JdbcTemplate jdbcTemplate;
    private final TransCoordination transCoordination;

    @Value("${kakao.rest-api-key}")
    private String kakaoApiKey;

    public void recommend(CategoryRequestDto request, HttpSession session) {
        List<String> categoryList = request.getCategoryList();
        FoodCategory category = new FoodCategory();

        if (categoryList != null) {
            for (String categoryElement : categoryList) {
                switch (categoryElement) {
                    case "한식":
                        category.setKorean(true);
                        break;
                    case "일식":
                        category.setJapanese(true);
                        break;
                    case "중식":
                        category.setChinese(true);
                        break;
                    case "양식":
                        category.setWestern(true);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown category: " + categoryElement);
                }
            }
        }
        categoryRepository.save(category);

        // 사용자 선택을 세션에 저장
        session.setAttribute("selectedCategory", category);
        System.out.println("Saved in session: " + session.getAttribute("selectedCategory"));
    }

    public FoodCategory getLastCategory() {
        return categoryRepository.findAll().stream().findFirst().orElse(null); // 단일 객체 반환
    }

    public List<RestaurantResponseDto> AroundRestaurants(double currentLat, double currentLon) { // List<String> selectedCategories
        List<Restaurant> restaurants = restaurantRepository.findAll();
//        Distance distanceCalculator = new Distance();
//        double maxDistance = 100.0;
        List<Restaurant> nearbyRestaurants = new ArrayList<>();

        // 세션에서 선택된 카테고리 가져오기
//        FoodCategory selectedCategory = (FoodCategory) session.getAttribute("selectedCategory");
//        if(selectedCategory == null){
//            throw new IllegalArgumentException("선택된 카테고리가 없습니다.");
//        }
//        if (selectedCategories == null || selectedCategories.isEmpty()) {
//            throw new IllegalArgumentException("선택된 카테고리가 없습니다.");
//        }

        for (Restaurant restaurant : restaurants) {
            // 거리 구하기
//            double distance = distanceCalculator.distance(currentLat, currentLon,
//                restaurant.getLatitude(), restaurant.getLatitude());

            // 100m 이내 인 조건 //  카테고리 조건 추가
//                if (distance <= maxDistance ) { // matchesCategory(restaurant, selectedCategories)
                nearbyRestaurants.add(restaurant);
//            }
        }
//        for(Restaurant restaurant : nearbyRestaurants){
//            System.out.println("100m 이내의 식당 : " + restaurant.getName());
//        }

//        return nearbyRestaurants;
//         랜덤 10개 방법 1
//        List<Restaurant> randomRestaurants =  selectRandomRestaurants(nearbyRestaurants);
//        랜덤 10개 방법 2
        Collections.shuffle(nearbyRestaurants);
        List<Restaurant> randomRestaurants = nearbyRestaurants.stream()
            .limit(10)
            .collect(Collectors.toList());

        return randomRestaurants.stream()
            .map(r -> new RestaurantResponseDto(r.getName(), r.getCategory(), r.getAddress(), r.getNumber()))
            .collect(Collectors.toList());
    }

    private boolean matchesCategory(Restaurant restaurant, List<String> foodCategory){
       for( String category : foodCategory){
        if(category.equals("한식") && "한식".equals(restaurant.getCategory())){
               return true;
           }
           if(category.equals("일식") && "일식".equals(restaurant.getCategory())){
               return true;
           }
           if(category.equals("중식") && "중식".equals(restaurant.getCategory())){
               return true;
           }
           if(category.equals("양식") && "양식".equals(restaurant.getCategory())){
               return true;
           }
       }
        return false;
    }

    public List<Restaurant> selectRandomRestaurants(List<Restaurant> restaurants) {
        List<Restaurant> randomRestaurants = new ArrayList<>();
        Random random = new Random();
        int count = 10;
        int totalSize = restaurants.size();
        if (totalSize <= count) {
            return restaurants;
        }
        while (randomRestaurants.size() < count) {
            int index = random.nextInt(totalSize);
            Restaurant restaurant = restaurants.get(index);
            if (!randomRestaurants.contains(restaurant)) {
                randomRestaurants.add(restaurant);
            }
        }
        return randomRestaurants;
    }

}







