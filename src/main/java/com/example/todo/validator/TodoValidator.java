package com.example.todo.validator;

import com.example.todo.entity.Todo;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TodoValidator {


    public static List<String> validateTodo(Todo todo) {
        List<String> errors = new ArrayList<>();
        if(todo == null) {
            errors.add("Inserire il titolo");
            return errors;
        }
        if(StringUtils.isEmpty(todo.getTitle())) {
            errors.add("Inserire il titolo");
        }
        return errors;
    }
}
