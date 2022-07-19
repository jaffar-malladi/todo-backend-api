package com.todos.controllers;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.todos.exceptions.ResourceNotFoundException;
import com.todos.models.Todo;
import com.todos.services.TodosServices;

@RestController
@RequestMapping("/api/v1/todos")
public class TodosController {

	@Autowired
	private TodosServices todoService;
	
	@GetMapping()
	public List<Todo> getAllTodos() {
		List<Todo> allTodos = todoService.getAllTodos();
		
		for(Todo todo: allTodos) {
			int todoId = todo.getId();
			Link selfLink = WebMvcLinkBuilder.linkTo(TodosController.class).slash(todoId).withSelfRel();
			todo.add(selfLink);
		}
		return allTodos;
	}
	
	@GetMapping("/user/{name}")
	public List<Todo> getTodoByUser(@PathVariable String name) {
		List<Todo> userTodos = todoService.getTodosByUser(name);

		if(userTodos.isEmpty()) {
			throw new ResourceNotFoundException("Request Not found");
		}
		return userTodos;
	}
	
	@GetMapping("/{id}")

    public Todo getTodoById(@PathVariable int id) {

        Todo todo = todoService.getTodoById(id);

        if(todo==null) {

            throw new ResourceNotFoundException("Request Not Found");

        }

        return todo;

    }

	//POST
	@PostMapping()
	public ResponseEntity<Todo> addTodo(@Valid @RequestBody Todo todo) {
		Todo newTodo = todoService.addTodo(todo);
		
		if(newTodo == null) {
			return ResponseEntity.noContent().build();
		}
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newTodo.getId()).toUri();
        return ResponseEntity.created(location).build();
		
	}
   
	//PUT - update the existing todos based on the user name shared in the pathvariable
	//Location:8080/todos/user/{name}/id
	// if the name and the todo is not realeted it should throw error
	
	@PutMapping("user/{name}/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String name, @PathVariable int id, @Valid @RequestBody Todo todo) {
        Todo updatedTodo = todoService.updateTodo(name, id, todo);
        if(updatedTodo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No todo for giving Name and id");
        }
        return new ResponseEntity<Todo>(updatedTodo,HttpStatus.OK);
    }
	
	//DELETE - delete the resource based on id
	//Location - localhost:8080/todos/{id}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable int id) {
        boolean result = todoService.deleteTodo(id);
        if (!result) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return new ResponseEntity<String>("Successfully Deleted Todo", HttpStatus.OK);
    }
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->
        {
            String fieldName =  ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
