package com.example.todo.validator;

import com.example.todo.dto.AuthDTO;
import com.example.todo.dto.CredentialsDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CredentialsValidator {

    public static List<String> validateCredentials(AuthDTO credentials) {
        List<String> errors = new ArrayList<>();
        if(credentials == null) {
            errors.add("Inserire lo username");
            errors.add("Inserire la password");
            return errors;
        }
        if(StringUtils.isEmpty(credentials.getUsername())) {
            errors.add("Inserire lo username");
        }
        if(StringUtils.isEmpty(credentials.getPassword())) {
            errors.add("Inserire la password");
        }
        return errors;
    }
}
