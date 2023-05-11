package exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BussinessExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(BussinessException.class)
    public ResponseEntity<Map<String, String>> handleBussinessException(BussinessException bussinessException) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", bussinessException.getErrorMessage());
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleBussinessException(RuntimeException bussinessException) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", bussinessException.getMessage());
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }


}
