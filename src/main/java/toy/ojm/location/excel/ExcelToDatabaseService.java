//package toy.ojm.location.excel;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import toy.ojm.controller.dto.OJMRequestDto;
//import toy.ojm.entity.Restaurant;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import toy.ojm.location.Coordinates;
//import toy.ojm.service.SaveToDatabaseService;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class ExcelToDatabaseService {
//    // RestaurantDTO 객체 리스트를 RestaurantEntity 객체 리스트로 변환하고 데이터베이스에 저장하거나 읽어올 때 사용된다.
//    // 엑셀에서 데이터를 읽어와 DB에 저장하거나 DB에서 데이터를 읽어올 때 사용된다.
//
//    private final ExcelReader excelReader;  // Excel 파일을 읽는 데 사용되는 객체. 인스턴스 선언
//    private final SaveToDatabaseService saveToDatabaseService;
//
//    // Excel 파일을 읽어와서 데이터베이스에 저장하는 메서드
//    public void readFromExcelAndSave(String filePath, OJMRequestDto request) {
//        List<ExcelRestaurantData> excelRestaurants = excelReader.read(filePath); // Excel 파일에서 읽어온 데이터를 RestaurantDTO 리스트인 excelRestaurants에 저장한다.
//        List<Restaurant> restaurantEntities = convertRestaurantEntities(excelRestaurants);  // RestaurantDTO 리스트인 excelRestaurants를 RestaurantEntity 리스트인 restaurantEntities로 변환하여 저장.
//
//        saveToDatabaseService.saveRestaurants(restaurantEntities);
//    }
//
//    // RestaurantDTO 리스트를 RestaurantEntity 리스트로 변환하는 메서드
//    private List<Restaurant> convertRestaurantEntities(List<ExcelRestaurantData> excelRestaurants) { // RestaurantDTO 리스트인 excelRestaurants를 전달값으로 받는 RestaurantEntity 리스트인 convertRestaurantEntities 메서드
//        List<Restaurant> restaurantEntities = new ArrayList<>(); // RestaurantEntity 리스트 타입의 restaurantEntities을 생성한다. ArrayList로 생성한다.
//        for (ExcelRestaurantData excelRestaurant : excelRestaurants) { // RestaurantDTO를 돈다. excelRestaurants하나하나로 돈다. 돈 것을 excelRestaurant 여기에 저장한다.
//            try {
//                // RestaurantEntity 객체 생성 및 데이터 설정(entity에 저장)
//                Restaurant entity = new Restaurant();
//                entity.setBusinessStatus(excelRestaurant.getBusinessStatus());
//                entity.setStreetNumberAddress(excelRestaurant.getStreetNumberAddress());
//                entity.setRestaurantName(excelRestaurant.getRestaurantName());
//                entity.setCategory(excelRestaurant.getCategory());
//                entity.setLongitude(excelRestaurant.getLongitude());
//                entity.setLatitude(excelRestaurant.getLatitude());
//                restaurantEntities.add(entity);
//            } catch (Exception e) {
//                log.error("Error creating RestaurantEntity: {}", e.getMessage());
//            }
//        }
//        return restaurantEntities; // 변환된 RestaurantEntity 리스트를 반환
//    }
//
//
//    // 유효한 좌표인지 확인하는 메서드
//    private boolean isValidCoordinates(Coordinates coordinates) {
//        if (coordinates == null) {
//            return false;
//        }
//        return coordinates.getLongitude() != null && coordinates.getLatitude() != null;
//    }
//}
