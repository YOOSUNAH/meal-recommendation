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

    private boolean Korean = false;
    private boolean Japanese = false;
    private boolean Chinese = false;
    private boolean Western = false;
    private boolean etc = false;

    public void setKorean(boolean korean) {
        this.Korean = korean;
    }

    public void setJapanese(boolean japanese) {
        this.Japanese = japanese;
    }

    public void setChinese(boolean chinese) {
        this.Chinese = chinese;
    }

    public void setWestern(boolean western) {
        this.Western = western;
    }

    public void setEtc(boolean etc) {
        etc = true;}
}
