package com.assetmanagement.backend.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", exception.getMessage());
        response.put("status", exception.getStatus().value());
        return ResponseEntity.status(exception.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Dữ liệu gửi lên không hợp lệ.");
        response.put(
            "errors",
            exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                    fieldError -> fieldError.getField(),
                    fieldError -> fieldError.getDefaultMessage(),
                    (left, right) -> left
                ))
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", resolveConstraintMessage(exception));
        response.put("status", HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpectedException(Exception exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hệ thống gặp lỗi không mong muốn.");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private String resolveConstraintMessage(DataIntegrityViolationException exception) {
        String message = exception.getMostSpecificCause() != null
            ? exception.getMostSpecificCause().getMessage()
            : exception.getMessage();

        if (message == null) {
            return "Dữ liệu lưu vào hệ thống bị trùng hoặc không hợp lệ.";
        }

        String normalizedMessage = message.toLowerCase();
        if (normalizedMessage.contains("uk_assets_asset_code")) {
            return "Mã tài sản đã tồn tại.";
        }
        if (normalizedMessage.contains("uk_assets_serial_number")) {
            return "Số serial đã tồn tại.";
        }
        if (normalizedMessage.contains("uk_assets_asset_tag")) {
            return "Mã thẻ tài sản đã tồn tại.";
        }

        return "Dữ liệu lưu vào hệ thống bị trùng hoặc không hợp lệ.";
    }
}
