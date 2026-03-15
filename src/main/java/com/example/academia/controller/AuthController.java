package com.example.academia.controller;

import com.example.academia.dto.LoginRequest;
import com.example.academia.dto.UsuarioResponseDTO;
import com.example.academia.dto.ActivacionDTO;
import com.example.academia.dto.JwtResponse;
import com.example.academia.security.JwtTokenProvider;
import com.example.academia.security.UserDetailsImpl;
import com.example.academia.service.RegistroService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RegistroService registroService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, RegistroService registroService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.registroService = registroService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Obtener roles
        String[] roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toArray(String[]::new);

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail(), roles));
    }

    @PostMapping("/activar")
    public ResponseEntity<Void> activarCuenta(@Valid @RequestBody ActivacionDTO dto) {
        registroService.activarCuenta(dto.getEmail(), dto.getCurrentPasword(), dto.getNewPasword());
        return ResponseEntity.ok().build();
    }
}