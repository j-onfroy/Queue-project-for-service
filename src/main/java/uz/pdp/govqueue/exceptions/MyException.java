package uz.pdp.govqueue.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MyException extends RuntimeException {

    private final HttpStatus status;
    private final String msg;

    public MyException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
        this.msg = msg;
    }
}
