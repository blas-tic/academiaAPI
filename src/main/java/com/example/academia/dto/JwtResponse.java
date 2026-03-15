package com.example.academia.dto;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String[] roles;

    
    public JwtResponse(String token, Long id, String email, String[] roles) {
      this.token = token;
      this.email = email;
      this.roles = roles;
      this.id = id;
   }


    public String getToken() {
       return token;
    }
    public void setToken(String token) {
       this.token = token;
    }
    public String getType() {
       return type;
    }
    public void setType(String type) {
       this.type = type;
    }
    public String getEmail() {
       return email;
    }
    public void setEmail(String email) {
       this.email = email;
    }
    public String[] getRoles() {
       return roles;
    }
    public void setRoles(String[] roles) {
       this.roles = roles;
    }
    public Long getId() {
       return id;
    }

    
}
