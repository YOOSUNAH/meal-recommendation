package toy.ojm.domain.service;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import toy.ojm.domain.dto.CategoryRequestDto;
import toy.ojm.domain.entity.FoodCategory;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class OJMService {

    private final CategoryRepository categoryRepository;
    private final RestTemplate restTemplate;

    @Value("${kakao.rest-api-key}")
    private String kakaoApiKey;



    public void recommend(CategoryRequestDto request, HttpSession session) {

        List<String> categoryList = request.getCategoryList();
        FoodCategory category = new FoodCategory();

        if (categoryList != null) {
            for (String categoryElement : categoryList) {
                switch (categoryElement) {
                    case  "한식":
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

    public List<Restaurant> getRecommendedRestaurants(double latitude, double longitude, List<String> categories) {
        // 카카오 맵 API를 호출하여 검색 결과를 받아옴
        List<Restaurant> restaurants = new ArrayList<>();
        for (String category : categories) {
            String url = "https://dapi.kakao.com/v2/local/search/keyword.json?y=" + latitude + "&x=" + longitude +
                "&query=" + category + "&apikey=" + kakaoApiKey;

            // API 호출
            KakaoApiResponse response = restTemplate.getForObject(url, KakaoApiResponse.class);

            // 응답 결과를 Restaurant 엔티티로 변환하여 반환
            if (response != null && response.getDocuments() != null) {
                restaurants.addAll(convertToRestaurants(response.getDocuments()));
            }
        }

        // 랜덤하게 10개의 식당 선택하여 반환
        Collections.shuffle(restaurants);
        return restaurants.subList(0, Math.min(restaurants.size(), 10));
    }

    // 카카오 API 응답을 Restaurant 엔티티로 변환하는 메서드
    private List<Restaurant> convertToRestaurants(KakaoApiDocument[] documents) {
        return Arrays.stream(documents)
            .map(doc -> {
                Restaurant restaurant = new Restaurant();
                restaurant.setRestaurantName(doc.getPlaceName());
                restaurant.setAddress(doc.getAddressName());
                restaurant.setLatitude(doc.getY());
                restaurant.setLongitude(doc.getX());
                return restaurant;
            })
            .toList();
    }



    // 카카오 API 응답 데이터 구조를 매핑하기 위한 클래스
    private static class KakaoApiResponse {
        private KakaoApiDocument[] documents;
        public KakaoApiDocument[] getDocuments() {
            return documents;
        }
    }
    // 카카오 API 응답 데이터 구조를 매핑하기 위한 클래스
    private static class KakaoApiDocument {
        private String placeName;
        private String addressName;
        private double x;
        private double y;

        public String getPlaceName() {
            return placeName;
        }

        public String getAddressName() {
            return addressName;
        }

        public double getX() {
            return x;
        }
        public double getY() {
            return y;
        }
    }
}







