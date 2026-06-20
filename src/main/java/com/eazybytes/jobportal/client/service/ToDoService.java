package com.eazybytes.jobportal.client.service;

import com.eazybytes.jobportal.dto.ToDoDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;

import java.util.List;

@HttpExchange// (url="https://jsonplaceholder.typicode.com/todos")
public interface ToDoService {

    @GetExchange
    List<ToDoDto> findAll();

    @GetExchange("/{id}")
    ToDoDto findById(@PathVariable Long id);

    @PostExchange
    ToDoDto create(@RequestBody ToDoDto post);

    @PutExchange("/{id}")
    ToDoDto update(@PathVariable Long id, @RequestBody ToDoDto post);

    @DeleteExchange("/{id}")
    void delete(@PathVariable Long id);
}
