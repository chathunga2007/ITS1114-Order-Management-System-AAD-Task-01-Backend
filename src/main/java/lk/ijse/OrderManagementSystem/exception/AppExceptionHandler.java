package lk.ijse.OrderManagementSystem.exception;

import lk.ijse.OrderManagementSystem.constant.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public CommonResponse handleServerException(Exception e, WebRequest request) {
        e.printStackTrace();
        return new CommonResponse(500, "Unexpected error occurred");
    }

    @ExceptionHandler(value = {CustomerException.class})
    public ResponseEntity<CommonResponse> handleCustomException(CustomerException e, WebRequest request) {
        e.printStackTrace();
        return new ResponseEntity<CommonResponse>(new CommonResponse(e.getCode(), e.getMessage()), HttpStatus.OK);
    }
}