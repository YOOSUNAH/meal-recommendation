package toy.ojm.entity;

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

    private boolean Korean = false;
    private boolean Japanese = false;
    private boolean Chinese = false;
    private boolean Western = false;


    public void setKorean(boolean korean) {
        this.Korean = true;
    }

    public void setJapanese(boolean japanese) {
        Japanese = true;
    }

    public void setChinese(boolean chinese) {
        Chinese = true;
    }

    public void setWestern(boolean western) {
        Western = true;
    }
}
