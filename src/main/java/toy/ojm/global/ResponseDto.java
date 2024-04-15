package toy.ojm.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    private T data;

    public static <T> ResponseEntity<ResponseDto<T>> of(
        HttpStatus status, T data
    ) {
        return ResponseEntity.status(status).body(new ResponseDto<>(data));
    }
}
