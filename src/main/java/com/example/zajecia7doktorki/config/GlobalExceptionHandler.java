package com.example.zajecia7doktorki.config;

import com.example.zajecia7doktorki.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
//        List<String> errors = ex.getBindingResult().getFieldErrors()
//                .stream()
//                .map(FieldError::getDefaultMessage).toList();
//        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
//    }
//
//    private Map<String, List<String>> getErrorsMap(List<String> errors) {
//        Map<String, List<String>> errorResponse = new HashMap<>();
//        errorResponse.put("errors", errors);
//        return errorResponse;
//    }
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//                                                                  @NonNull HttpHeaders headers,
//                                                                  @NonNull HttpStatus status,
//                                                                  @NonNull WebRequest request) {
//        Map<String, String> errorDetails = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errorDetails.put(fieldName, errorMessage);
//        });
//        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorDetails);
//        logger.error(ex.getMessage());
//        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
//    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        Map<String, String> errorDetails = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorDetails);
        logger.error(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AppointmentNotFoundException.class, PatientNotFoundException.class, DoctorNotFoundException.class})
    protected ResponseEntity<ApiError> handleNotFoundException(RuntimeException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        logger.error(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {DoctorAlreadyExistsException.class, PatientAlreadyExistsException.class})
    protected ResponseEntity<ApiError> handleDuplicateEntityException(RuntimeException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        logger.error(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(value = {AppointmentConflictException.class})
    protected ResponseEntity<ApiError> handleConflictException(RuntimeException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        logger.error(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

}
