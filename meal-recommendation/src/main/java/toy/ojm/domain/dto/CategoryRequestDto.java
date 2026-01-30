package toy.ojm.domain.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategoryRequestDto {
  private final List<String> categoryList;
}
