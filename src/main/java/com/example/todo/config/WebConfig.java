package com.example.todo.config;

import com.example.todo.config.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;
    // Aggiungiamo il nostro AuthInterceptor alla catena degli Interceptor
    // Solo gli end-point che iniziano con /api verranno sottoposti al controllo dell'Interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("WebConfig: Registrazione dell'Interceptor");
        registry.addInterceptor(authInterceptor).addPathPatterns("/api/**"); // Protegge gli endpoint
    }
}
