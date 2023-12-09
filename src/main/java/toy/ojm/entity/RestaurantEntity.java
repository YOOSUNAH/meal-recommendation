package toy.ojm.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String DTLSTATENM;
    private String SITEWHLADDR;
    private String RDNWHLADDR;
    private String BPLCNM;
    private String UPTAENM;
    private String X;
    private String Y;
}
