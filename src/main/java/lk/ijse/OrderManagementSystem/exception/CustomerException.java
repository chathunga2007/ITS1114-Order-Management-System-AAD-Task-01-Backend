package lk.ijse.OrderManagementSystem.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CustomerException extends RuntimeException{
    private int code;
    private String message;
}