package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.repository.TodoRepository;
import com.example.todo.validator.TodoValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public Todo create(Todo todo, HttpServletRequest request) {
        List<String> errors = TodoValidator.validateTodo(todo);
        if(!errors.isEmpty()) {
            String message = "Category is not valid";
            for(String e: errors) {
                message += "; " + e;
            }
            throw new RuntimeException(message);
        }
        User user = (User) request.getAttribute("user");
        todo.setUser(user);
        return todoRepository.save(todo);
    }

    public Todo save(Todo todo) {
        List<String> errors = TodoValidator.validateTodo(todo);
        if(!errors.isEmpty()) {
            String message = "Category is not valid";
            for(String e: errors) {
                message += "; " + e;
            }
            throw new RuntimeException(message);
        }
        return todoRepository.save(todo);
    }

    public List<Todo> findAll(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return todoRepository.findByUser(user);
    }

    public Todo findById(Long id, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        final Optional<Todo> todo = todoRepository.findByIdAndUser(id, user);

        return todo.
                orElseThrow(() -> new EntityNotFoundException("No Todo found with ID = " + id));
    }

    public void delete(Long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        if (todo.isEmpty()) {
            throw new EntityNotFoundException("No Todo found with ID = " + id);
        }
        todoRepository.deleteById(id);
    }
}
