package toy.ojm.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class FoodCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private FoodCategoryName category;

    public void setCategory(String categoryName) {
        try {
            this.category = FoodCategoryName.fromString(categoryName);
        } catch (IllegalArgumentException e) {
            this.category = FoodCategoryName.ETC; // 한식, 일식, 중식, 양식에 해당하지 않으면 기타로 설정
        }
    }
}
