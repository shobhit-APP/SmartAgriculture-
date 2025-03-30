
package com.example.AgriConnect.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnyException extends RuntimeException {
    private String errorMessage;
    private HttpStatus status;
    public AnyException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
