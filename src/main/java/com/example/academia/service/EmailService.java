package com.example.academia.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
   private static final Logger log = LoggerFactory.getLogger(EmailService.class);
   private final JavaMailSender mailSender;

   public EmailService(JavaMailSender mailSender) {
      this.mailSender = mailSender;
   }

   public void enviarPassword(String destinatario, String password) {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(destinatario);
      message.setSubject("Bienvenido a la Academia - Credenciales de acceso");
      message.setText("Hola,\n\nSe ha creado una cuenta para usted en la Academia.\n" +
            "Su contraseña temporal es: " + password + "\n\n" +
            "Por favor, active su cuenta (/api/auth/activar) y cambie su contraseña para después poder hacer login (/api/auth/login).\n\n" +
            "Saludos.");

      try {
         mailSender.send(message);
         log.info("Email enviado a {}", destinatario);
      } catch (Exception e) {
         log.error("Error al enviar email a {}: {}", destinatario, e.getLocalizedMessage());
      }
   }

   public void enviarConfirmacion(String destinatario) {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(destinatario);
      message.setSubject("Academia - Activación");
      message.setText("Hola,\n\nHas cambiado tu contraseña y tu usuario se ha activado.\n\n" +
            "Puedes iniciar sesión (/api/auth/login).\n\n" +
            "Saludos.");

      try {
         mailSender.send(message);
         log.info("Email enviado a {}", destinatario);
      } catch (Exception e) {
         log.error("Error al enviar email a {}: {}", destinatario, e.getLocalizedMessage());
      }
   }

}
