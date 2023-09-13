package toy.ojm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListDto {
    private int index;
    private String category;
    private String name;
   private String longitude;
   private String latitude;
    private String signatureMenu;
}
