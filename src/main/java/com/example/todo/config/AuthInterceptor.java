package com.example.todo.config;

import com.example.todo.entity.User;
import com.example.todo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private AuthService authService;
    // Il metodo restituisce false se l'autenticazione fallisce (token assente o token non valido)
    // Il metodo restituisce true se l'autenticazione va a buon fine
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("AuthInterceptor: preHandle called");
        // In questo punto recuperiamo il token dall'header della request
        String token = request.getHeader("Authorization");
        token = token.replace("Bearer ","").trim();// Recuperiamo il token dall'header
        System.out.println(token);
        if (token == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token mancante");
            return false;
        }
        // Ogni volta che eseguiamo il login viene creata in memoria una coppia token-utente
        // In fase di utilizzo degli end-point dell'app che richiedono autenticazione verifichiamo se esiste
        // una coppia utente-token valida recuperando l'utente dalla sessione tramite token
        User user = authService.getUserFromToken(token); // Recuperiamo l'utente dalla sessione
        // Se l'utente e' null, non esiste una coppia valida utente-token, l'autorizzazione fallisce
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token non valido o sessione scaduta");
            return false;
        }
        // Se l'autenticazione va a buon fine, l'utente recuperato tramite token viene inserito nella request verso l'end-point richiamato
        request.setAttribute("user", user); // Salviamo l'utente nella richiesta
        return true;
    }
}
