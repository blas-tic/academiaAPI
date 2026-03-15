package com.example.academia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(ResourceNotFoundException.class)
   public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("status", HttpStatus.NOT_FOUND.value());
      body.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
      body.put("message", ex.getMessage());
      body.put("path", request.getDescription(false).replace("uri=", ""));
      return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
   }

   // Opcional: manejar otras excepciones comunes
   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("status", HttpStatus.BAD_REQUEST.value());
      body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
      body.put("message", ex.getMessage());
      body.put("path", request.getDescription(false).replace("uri=", ""));
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<?> handleGenericException(Exception ex, WebRequest request) {
      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
      body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      body.put("message", "Error interno del servidor");
      body.put("path", request.getDescription(false).replace("uri=", ""));
      return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
   }

   @ExceptionHandler(EmailAlreadyExistsException.class)
   public ResponseEntity<?> handleEmailAlreadyExists(EmailAlreadyExistsException ex, WebRequest request) {
      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("status", HttpStatus.BAD_REQUEST.value());
      body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
      body.put("message", ex.getMessage());
      body.put("path", request.getDescription(false).replace("uri=", ""));
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(InvalidPersonTypeException.class)
   public ResponseEntity<?> handleInvalidPersonType(InvalidPersonTypeException ex, WebRequest request) {
      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("status", HttpStatus.BAD_REQUEST.value());
      body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
      body.put("message", ex.getMessage());
      body.put("path", request.getDescription(false).replace("uri=", ""));
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("status", HttpStatus.BAD_REQUEST.value());
      body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());

      // Recoger todos los errores de campo
      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
      body.put("errors", errors);

      body.put("path", request.getDescription(false).replace("uri=", ""));
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
   }
}