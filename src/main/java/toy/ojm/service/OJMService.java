package toy.ojm.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.ojm.controller.dto.CategoryRequestDto;
import toy.ojm.entity.FoodCategory;
import toy.ojm.repository.CategoryRepository;

/**
 * TODO: 규칙 수행
 * 1. 사용자가 요청한 카테고리만 필터링할 것
 * List<ListDto> ListDtoList = naverAPI.search(request.getCoordinates());
 * 2. 음식점 랜덤으로 10개 추출
 * for (categoryList categoryList : categoryListList) {}
 */

@Service
@RequiredArgsConstructor
public class OJMService {

    private final CategoryRepository categoryRepository;

//    public static final int LIST_LIMIT_COUNT = 10;
//    private static final Logger log = LoggerFactory.getLogger(OJMService.class);

    public void recommend(CategoryRequestDto request) {

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
    }

//    public List<OJMResponseDto.Item> filterByCategory(Coordinates coordinates, List<String> requestedCategoryList) {
//        List<Restaurant> nearbyRestaurants = nearbyRestaurantService.getNearbyRestaurants(coordinates);
//        List<Restaurant> filteredRestaurants;
//
//        filteredRestaurants = nearbyRestaurants.stream()
//            .filter(restaurant -> {
//                String category = restaurant.getCategory();
//                return category != null && requestedCategoryList.contains(category);
//            })
//            .collect(Collectors.toList());
//        return convertToItems(filteredRestaurants);
//    }
//
//    private List<OJMResponseDto.Item> convertToItems(List<Restaurant> restaurantEntities) {
//        return restaurantEntities.stream().map(this::convertToItem).collect(Collectors.toList());
//    }
//
//    private OJMResponseDto.Item convertToItem(Restaurant restaurant) {
//        OJMResponseDto.Item item = new OJMResponseDto.Item();
//        item.setCategory(restaurant.getCategory());
//        return item;
//    }
//
//    // 랜덤으로 10개 음식점 선택
//    public List<Restaurant> randomize(List<Restaurant> restaurantEntities) {
//        List<Restaurant> randomList = new ArrayList<>(restaurantEntities);
//        Collections.shuffle(randomList);   // randomList의 요소들을 무작위로 섞는다.
//        return randomList.subList(0, Math.min(LIST_LIMIT_COUNT, randomList.size()));
//    }
//
//
//    @Transactional
//    public List<ContentDto> listMyMap(Double x, Double y) {
//
//        // Location 자료형으로 변수를 선언하여 해당 요청받은 x,y 값으로 북동쪽과 남서쪽의 위치를 계산
//        Coordinates northEast = GeometryUtil.calculate(x, y, 2.0, Direction.NORTHEAST.getBearing());
//        Coordinates southWest = GeometryUtil.calculate(x, y, 2.0, Direction.SOUTHWEST.getBearing());
//
//        // 이를 바탕으로 NativeQuery로 북동쪽, 남서쪽 거리를 String으로 작성
//        String pointFormat = String.format(
//            "'LINESTRING(%f %f, %f %f)'",
//            northEast.getLatitude(), northEast.getLongitude(), southWest.getLatitude(), southWest.getLongitude()
//        );
//
//
//    }
//
//
//
//
//
//    @Transactional(readOnly = true)
//    public List<Restaurant> getNearByRestaurants(Double latitude, Double longitude, Double distance) {
//        Coordinates northEast = GeometryUtil
//            .calculate(latitude, longitude, distance, Direction.NORTHEAST.getBearing());
//        Coordinates southWest = GeometryUtil
//            .calculate(latitude, longitude, distance, Direction.SOUTHWEST.getBearing());
//
//        double x1 = northEast.getLatitude();
//        double y1 = northEast.getLongitude();
//        double x2 = southWest.getLatitude();
//        double y2 = southWest.getLongitude();
//
//        String pointFormat = String.format("'LINESTRING(%f %f, %f %f)')", x1, y1, x2, y2);
//        Query query = em.createNativeQuery("SELECT r.id, r.address, r.address_city, "
//                + "r.address_district, r.address_district_old, r.address_old, r.address_province, "
//                + "r.category, r.category_code, r.category_industry, r.category_main, r.category_sub, "
//                + "r.point, r.name, r.zip_code "
//                + "FROM restaurant AS r "
//                + "WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + pointFormat + ", r.point)", Restaurant.class)
//            .setMaxResults(10);
//
//        List<Restaurant> restaurants = query.getResultList();
//        return restaurants;
//    }
}



