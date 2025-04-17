package toy.ojm.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CategoryRequestDto {

    private final List<String> categoryList;
}
