package toy.ojm.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestaurantResponseDto {
  private final String name; // 사업장명
  private final String category; // 업태구분명
  private final String address; // 지번주소
  private final String number; // 전화번호
}
