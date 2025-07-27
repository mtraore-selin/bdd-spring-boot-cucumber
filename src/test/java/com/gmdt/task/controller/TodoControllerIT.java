package com.gmdt.task.controller;

import com.gmdt.task.AbstractPostgresContainerTest;
import com.gmdt.task.model.Todo;
import com.gmdt.task.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerIT extends AbstractPostgresContainerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void cleanDatabase() {
        todoRepository.deleteAll();
    }

    @Test
    void shouldCreateTodo() {
        // Arrange
        Todo newTodo = new Todo();
        newTodo.setTitle("Integration Test");
        newTodo.setCompleted(false);

        // Act
        ResponseEntity<Todo> response = restTemplate.postForEntity("/api/v1/todos", newTodo, Todo.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Integration Test");
    }

    @Test
    void shouldReturnAllTodos() {
        // Arrange
        Todo todo1 = Todo.builder().title("Integration Test 1").description("Integration Test 1").build();
        Todo todo2 = Todo.builder().title("Integration Test 2").description("Integration Test 2").build();
        todoRepository.save(todo1);
        todoRepository.save(todo2);

        // Act
        ResponseEntity<List<Todo>> response = restTemplate.exchange(
                "/api/v1/todos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void shouldReturnTodoById() {
        // Arrange
        Todo todo = Todo.builder().title("Task by ID Title").description("Task by ID Description").build();
        Todo saved = todoRepository.save(todo);

        // Act
        ResponseEntity<Todo> response = restTemplate.getForEntity("/api/v1/todos/" + saved.getId(), Todo.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Task by ID Title");
    }

    @Test
    void shouldDeleteTodo() {
        // Arrange
        Todo todo = Todo.builder().title("Task by ID Title").description("Task by ID Description").build();
        Todo saved = todoRepository.save(todo);

        // Act
        restTemplate.delete("/api/v1/todos/" + saved.getId());

        // Assert
        boolean exists = todoRepository.findById(saved.getId()).isPresent();
        assertThat(exists).isFalse();
    }


}
