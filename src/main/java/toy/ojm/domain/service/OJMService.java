package toy.ojm.domain.service;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import toy.ojm.domain.dto.CategoryRequestDto;
import toy.ojm.domain.entity.FoodCategory;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.repository.CategoryRepository;


@Service
@RequiredArgsConstructor
@Slf4j
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

    private List<Restaurant> selectRandomRestaurants(List<Restaurant> restaurants, int count) {
        List<Restaurant> randomRestaurants = new ArrayList<>();
        Random random = new Random();
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







