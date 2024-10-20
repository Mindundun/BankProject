package com.bankproject.bankproject.global.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<String> handleException(Exception e) {
    //     log.error("Exception Log Line: {}", e.getStackTrace()[0].getLineNumber());
    //     log.error("Exception Message: ", e.getMessage());
    //     log.error("Exception: ", e);
    //     return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    

    // /**
    //  * MethodArgumentNotValidException handler (Validation)
    //  */
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    //     // log 는 필요 없을듯
    //     Map<String, String> errors = new HashMap<>();
    //     ex.getBindingResult().getAllErrors().forEach(error -> {
    //         String fieldName = ((FieldError) error).getField();
    //         String errorMessage = error.getDefaultMessage();
    //         errors.put(fieldName, errorMessage);
    //     });
    //     return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    // }


}
