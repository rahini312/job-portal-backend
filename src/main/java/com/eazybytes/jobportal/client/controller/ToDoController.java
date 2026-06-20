package com.eazybytes.jobportal.client.controller;

import com.eazybytes.jobportal.client.service.ToDoService;
import com.eazybytes.jobportal.dto.ToDoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class ToDoController {

   // private final RestClientToDoService restClientTodoService;
    private final ToDoService todoService;

    @GetMapping
    ResponseEntity<List<ToDoDto>> findAll() {
        // return ResponseEntity.ok(restClientTodoService.findAll());
        return ResponseEntity.ok(todoService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<ToDoDto> findById(@PathVariable Long id) {
        // return ResponseEntity.ok(restClientTodoService.findById(id));
        return ResponseEntity.ok(todoService.findById(id));
    }

    @PostMapping
    ResponseEntity<ToDoDto> create(@RequestBody ToDoDto toDoDto) {
        // return ResponseEntity.status(HttpStatus.CREATED).body(restClientTodoService.create(toDoDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.create(toDoDto));
    }

    @PutMapping("/{id}")
    ResponseEntity<ToDoDto> update(@PathVariable Long id, @RequestBody ToDoDto toDoDto) {
        // return ResponseEntity.status(HttpStatus.OK).body(restClientTodoService.update(id, toDoDto));
        return ResponseEntity.status(HttpStatus.OK).body(todoService.update(id, toDoDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        // restClientTodoService.delete(id);
        todoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("ToDo deleted successfully");
    }
}
