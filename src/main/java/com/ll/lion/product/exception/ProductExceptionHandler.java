package com.ll.lion.product.exception;

import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.common.exception.DataNotFoundException;
import com.ll.lion.product.controller.ProductController;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice(basePackageClasses = ProductController.class)
public class ProductExceptionHandler {
    /**
     * 유효성 검증 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> validErrors = new HashMap<>();

        e.getBindingResult().getAllErrors()
                .forEach(c -> validErrors.put(((FieldError) c).getField(), c.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                new ResponseDto<>(e.getStatusCode().value(), null, e.getMessage(), null, validErrors)
        );
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> handleDataNotFoundExceptions(DataNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                base(HttpStatus.NOT_FOUND.value(), e.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error(e.getStackTrace());
        return ResponseEntity.internalServerError().body(
                base(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예상치 못한 에러")
        );
    }

    protected ResponseDto<?> base(int status, String message) {
        return new ResponseDto<>(status, null, message, null, null);
    }
}
