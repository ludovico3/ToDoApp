package com.example.todo.controller;

import com.example.todo.dto.AuthDTO;
import com.example.todo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
//locahost:8093/auth/register
//localhost:8093/auth/login
//localhost:8093/auth/logout
//localhost:8093/auth/deleteUser/
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDTO credentials) {
        try {
            String message = authService.register(credentials.getUsername(), credentials.getPassword());
            return ResponseEntity.ok(Map.of("message", message));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
    // Il login prende username e password e restituisce un token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO credentials) {
        try {
            String token = authService.login(credentials.getUsername(), credentials.getPassword());
            return ResponseEntity.ok(Map.of("token", token)); // 🔹 Ora il token è unico per ogni utente
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }
    // Inviamo il token e cancelliamo la coppia token-user dalla sessione nel logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ","").trim();
            authService.logout(token);
            return ResponseEntity.ok(Map.of("message", "Logout effettuato con successoo"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
    // Inviamo il token e cancelliamo la coppia token-user dalla sessione e l'utente dal db
    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) {
        try {
            authService.deleteUser(token);
            return ResponseEntity.ok(Map.of("message", "Utente eliminato con successo"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

}
