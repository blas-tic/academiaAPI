package com.example.academia.exception;

public class AccessDeniedException extends RuntimeException {

   public AccessDeniedException(String message) {
      super(message);
   }
}
