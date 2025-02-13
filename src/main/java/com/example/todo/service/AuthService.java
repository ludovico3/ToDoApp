package com.example.todo.service;

import com.example.todo.dto.AuthDTO;
import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.validator.CredentialsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    private Map<String, User> sessions = new HashMap<>(); // Mappa sessioni (token -> utente)
    private Map<String, String> userTokens = new HashMap<>(); // Mappa username -> token (per verificare se è già autenticato)
    // Se l'utente e' già presente nel db, lanciamo un'eccezione
    public String register(AuthDTO credentials) {
        List<String> errors = CredentialsValidator.validateCredentials(credentials);
        if(!errors.isEmpty()) {
            String message = "Category is not valid";
            for(String e: errors) {
                message += "; " + e;
            }
            throw new RuntimeException(message);
        }
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username già in uso!");
        }
        // Altrimenti, inseriamo l'utente nel db
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); //
        userRepository.save(newUser);

        return "Utente registrato con successo!";
    }
    // Recupera l'utente dal db
    public String login(AuthDTO credentials) {
        List<String> errors = CredentialsValidator.validateCredentials(credentials);

        if (!errors.isEmpty()) {    String message = "Category is not valid";
            for(String e: errors) {
                message += "; " + e;
            }
            throw new RuntimeException(message);
        }

        String username = credentials.getUsername();
        String password = credentials.getPassword();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();

            // 🔹 Se l'utente è già autenticato, restituiamo il token esistente
            if (userTokens.containsKey(user.getUsername())) {
                return userTokens.get(user.getUsername());
            }

            // 🔹 Altrimenti, generiamo un nuovo token e lo registriamo nella sessione
            String token = UUID.randomUUID().toString();
            sessions.put(token, user);
            userTokens.put(user.getUsername(), token);

            return token;
        }
        throw new RuntimeException("Credenziali errate");
    }

    public User getUserFromToken(String token) {
        return sessions.get(token);
    }
    // Nel logout prendiamo il token e verifichiamo se esiste una sessione legata a questo token
    public void logout(String token) {

        User user = sessions.get(token);
        // Se esiste un utente legato al token
        if (user != null) {
            userTokens.remove(user.getUsername()); // Rimuoviamo l'utente dalla mappa username-token
            sessions.remove(token); // Rimuoviamo il token dalla sessione
        } else {
            throw new RuntimeException("Nessun utente loggato o token non valido");
        }
    }
    // Nel delete verifichiamo che il token esista
    public void deleteUser(String token) {
        User user = sessions.get(token);
        // Se l'utente legato al token non esiste, l'utente non e' autenticato e non può essere cancellato
        if (user == null) {
            throw new RuntimeException("Utente non autenticato");
        }

        // Rimuove l'utente dal database
        userRepository.delete(user);

        // Rimuove la sessione dell'utente
        userTokens.remove(user.getUsername());
        sessions.remove(token);
    }

    public List<User> getAllUsers() {
      return userRepository.findAll();



    }


}

