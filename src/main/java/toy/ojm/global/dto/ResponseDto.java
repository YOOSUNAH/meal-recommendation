package toy.ojm.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    private T data;
    private String message;

    public static <T> ResponseEntity<ResponseDto<T>> of(
            HttpStatus status, T data
    ) {
        return ResponseEntity.status(status).body(new ResponseDto<>(data, null));
    }

    public static <T> ResponseEntity<ResponseDto<T>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(null, message));
    }
}

