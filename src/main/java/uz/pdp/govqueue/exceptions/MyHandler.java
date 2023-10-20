package uz.pdp.govqueue.exceptions;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.pdp.govqueue.payload.ApiResult;

@RestControllerAdvice
public class MyHandler {

    @ExceptionHandler(value = MyException.class)
    public HttpEntity<ApiResult<String>> handle(MyException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(new ApiResult<>(false, exception.getMsg()));
    }
}
