package com.example.academia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordChangeDTO {

   @NotBlank (message = "La contraseña actual es obligatoria")
   private String oldPassword;

   @NotBlank(message = "La nueva contraseña es obligatoria")
   @Size(min = 6, message = "La contraseña ha de tener al menos 6 caracteres")
   private String newPassword;

   public PasswordChangeDTO() {
   }

   public PasswordChangeDTO(@NotBlank(message = "La contraseña actual es obligatoria") String oldPassword,
         @NotBlank(message = "La nueva contraseña es obligatoria") @Size(min = 6, message = "La contraseña ha de tener al menos 6 caracteres") String newPassword) {
      this.oldPassword = oldPassword;
      this.newPassword = newPassword;
   }

   public String getOldPassword() {
      return oldPassword;
   }

   public void setOldPassword(String oldPassword) {
      this.oldPassword = oldPassword;
   }

   public String getNewPassword() {
      return newPassword;
   }

   public void setNewPassword(String newPassword) {
      this.newPassword = newPassword;
   }

   

}
