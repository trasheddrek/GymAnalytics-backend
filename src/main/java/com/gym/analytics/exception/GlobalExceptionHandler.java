package com.gym.analytics.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.gym.analytics.dto.ErrorResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalState(IllegalStateException ex) {
        ErrorResponseDto errResponse = ErrorResponseDto.builder()
                .mensaje("Operación no permitida")
                .errores(List.of(ex.getMessage()))
                .status(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errResponse);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex){
        List<String> err = ex
                            .getBindingResult()
                            .getFieldErrors()
                            .stream()
                            .map(e -> e.getField() + ": " + e.getDefaultMessage())
                            .toList();

        ErrorResponseDto errResponse = ErrorResponseDto
                                    .builder()
                                        .mensaje("Error de validación")
                                        .errores(err)
                                        .status(HttpStatus.BAD_REQUEST.value())
                                        .timestamp(LocalDateTime.now())
                                    .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
    }


    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleNotFound(NoHandlerFoundException ex) {

        ErrorResponseDto errResponse = ErrorResponseDto
                                        .builder()
                                            .mensaje("Recurso no encontrado")
                                            .errores(List.of(ex.getMessage()))
                                            .status(HttpStatus.NOT_FOUND.value())
                                            .timestamp(LocalDateTime.now())
                                        .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralError(Exception ex) {

        ErrorResponseDto errResponse = ErrorResponseDto
                                        .builder()
                                            .mensaje("Error interno del servidor")
                                            .errores(List.of(ex.getMessage()))
                                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                            .timestamp(LocalDateTime.now())
                                        .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errResponse);
    }    


    @ExceptionHandler(EmailYaExisteException.class)
    public ResponseEntity<ErrorResponseDto> emailYaExisteException(EmailYaExisteException ex) {

        ErrorResponseDto errResponse = ErrorResponseDto.builder()
                .mensaje("El email ya existe")
                .errores(List.of(ex.getMessage()))
                .status(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errResponse);
    }
}