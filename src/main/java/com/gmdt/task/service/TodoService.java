package com.gmdt.task.service;

import com.gmdt.task.model.Todo;
import com.gmdt.task.repository.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Todo findById(Long id) {
        return todoRepository.findById(id).orElseThrow();
    }

    public void delete(Long id) {
        todoRepository.deleteById(id);
    }

    public Todo update(Long id, Todo updatedTodo) {
        Todo existing = todoRepository.findById(id).orElseThrow();
        existing.setTitle(updatedTodo.getTitle());
        existing.setDescription(updatedTodo.getDescription());
        existing.setCompleted(updatedTodo.getCompleted());
        return todoRepository.save(existing);
    }
}
