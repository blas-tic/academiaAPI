package com.example.academia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ActivacionDTO {

   @NotBlank
   @Email
   private String email;

   @NotBlank
   private String currentPasword;

   @NotBlank @Size(min = 6, message = "La nueva contraseña debe tener al menos 6 caracteres")
   private String newPasword;


   public ActivacionDTO() {
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getCurrentPasword() {
      return currentPasword;
   }

   public void setCurrentPasword(String currentPasword) {
      this.currentPasword = currentPasword;
   }

   public String getNewPasword() {
      return newPasword;
   }

   public void setNewPasword(String newPasword) {
      this.newPasword = newPasword;
   }

   
}
