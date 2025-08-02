package com.gmdt.task.steps;

import com.gmdt.task.AbstractPostgresContainerTest;
import com.gmdt.task.model.Todo;
import com.gmdt.task.repository.TodoRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;


public class TodoSteps extends AbstractPostgresContainerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TodoRepository todoRepository;

    private Todo todo;
    private ResponseEntity<Todo> response;
    private ResponseEntity<Todo[]> listResponse;
    private Long savedTodoId;

    @Before
    public void resetDb() {
        todoRepository.deleteAll();
    }

    @Given("I have a todo with title {string} and completed {string}")
    public void iHaveATodo(String title, String completed) {
        todo = new Todo();
        todo.setTitle(title);
        todo.setCompleted(Boolean.parseBoolean(completed));
    }

    @When("I create the todo")
    public void iCreateTheTodo() {
        response = restTemplate.postForEntity("/api/v1/todos", todo, Todo.class);
        if (response.getBody() != null) savedTodoId = response.getBody().getId();
    }

    @Then("the todo should be saved successfully")
    public void theTodoShouldBeSavedSuccessfully() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(todo.getTitle(), response.getBody().getTitle());
    }

    @Given("there are {int} todos in the system")
    public void thereAreNTodosInSystem(int count) {
        for (int i = 0; i < count; i++) {
            Todo t = new Todo();
            t.setTitle("Task " + i);
            t.setCompleted(false);
            todoRepository.save(t);
        }
    }

    @When("I request all todos")
    public void iRequestAllTodos() {
        listResponse = restTemplate.getForEntity("/api/v1/todos", Todo[].class);
    }

    @Then("I should receive {int} todos")
    public void iShouldReceiveTodos(int count) {
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertNotNull(listResponse.getBody());
        assertEquals(count, listResponse.getBody().length);
    }

    @Given("I have a saved todo with title {string} and completed {string}")
    public void iHaveASavedTodo(String title, String completed) {
        Todo saved = new Todo();
        saved.setTitle(title);
        saved.setCompleted(Boolean.parseBoolean(completed));
        todo = todoRepository.save(saved);
        savedTodoId = todo.getId();
    }

    @When("I request the todo by ID")
    public void iRequestTheTodoByID() {
        response = restTemplate.getForEntity("/api/v1/todos/" + savedTodoId, Todo.class);
    }

    @Then("the response should contain the todo with title {string}")
    public void theResponseShouldContainTodo(String expectedTitle) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTitle, response.getBody().getTitle());
    }

    @When("I delete the todo")
    public void iDeleteTheTodo() {
        restTemplate.delete("/api/v1/todos/" + savedTodoId);
    }

    @Then("the todo should be removed from the system")
    public void theTodoShouldBeRemovedFromSystem() {
        boolean exists = todoRepository.findById(savedTodoId).isPresent();
        assertFalse(exists);
    }

    @When("I update the todo with title {string} and completed {string}")
    public void iUpdateTheTodo(String newTitle, String completed) {
        todo.setTitle(newTitle);
        todo.setCompleted(Boolean.parseBoolean(completed));
        restTemplate.put("/api/v1/todos/" + savedTodoId, todo);
        response = restTemplate.getForEntity("/api/v1/todos/" + savedTodoId, Todo.class);
    }

    @Then("the response should contain the todo with title {string} and completed {string}")
    public void theResponseShouldContainUpdatedTodo(String expectedTitle, String expectedCompleted) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTitle, response.getBody().getTitle());
        assertEquals(Boolean.parseBoolean(expectedCompleted), response.getBody().getCompleted());
    }

}
