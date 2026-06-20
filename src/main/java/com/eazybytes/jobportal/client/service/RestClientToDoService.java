package com.eazybytes.jobportal.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//This package is to call external APIs. For this we need to inject RestClient bean
public class RestClientToDoService {
   /* private static final String TODOS_API = "/todos";
    private final RestClient restClient;

    public RestClientToDoService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    public List<ToDoDto> findAll() {
        return restClient.get()
                .uri(TODOS_API)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) ->
                        new IllegalStateException("Failed to fetch todos"))
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public ToDoDto findById(Long id) {
        return restClient.get()
                .uri(TODOS_API + "/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) ->
                        new IllegalArgumentException("Todo not found with id: " + id))
                .body(ToDoDto.class);
    }

    public ToDoDto create(ToDoDto todo) {
        return restClient.post()
                .uri(TODOS_API)
                .body(todo)
                .retrieve()
                .body(ToDoDto.class);
    }

    public ToDoDto update(Long id, ToDoDto todo) {
        return restClient.put()
                .uri(TODOS_API + "/{id}", id)
                .body(todo)
                .retrieve()
                .body(ToDoDto.class);
    }

    public void delete(Long id) {
        restClient.delete()
                .uri(TODOS_API+ "/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }*/

}
