package uz.pdp.govqueue.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {

    private boolean success;

    private String message;//success : true

    private T data;

    public ApiResult(T data) {
        this.success = true;
        this.data = data;
    }

    public ApiResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
