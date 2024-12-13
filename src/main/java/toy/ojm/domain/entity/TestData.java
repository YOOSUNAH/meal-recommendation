package toy.ojm.domain.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestData {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uniqueKey;

    private String value;

    public TestData(String uniqueKey, String value) {
        this.uniqueKey = uniqueKey;
        this.value = value;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setUniqueKey(String uniqueKey){
        this.uniqueKey = uniqueKey;
    }

    public void setValue(String value){
        this.value = value;
    }
}
