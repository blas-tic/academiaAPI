package com.example.academia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AdminPasswordChangeDTO {

   @NotBlank(message = "La nueva contraseña es obligatoria")
   @Size(min = 6, message = "La contraseña ha de tener al menos 6 caracteres")
   private String newPassword;

   public AdminPasswordChangeDTO() {
   }


   public String getNewPassword() {
      return newPassword;
   }

   public void setNewPassword(String newPassword) {
      this.newPassword = newPassword;
   }

   

}
