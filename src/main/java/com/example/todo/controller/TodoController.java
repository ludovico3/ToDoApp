package com.example.todo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
// Funziona solo se l'utente e' autenticato 
//localhost:8093/api/prova
public class TodoController {

    @GetMapping("/prova")
    public String prova() {
        return "ciao";
    }
}
