package com.todos.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todos.models.Todo;
import com.todos.repositories.TodosRepository;
import com.todos.services.TodosServices;

@Service
public class TodoServiceDatabaseImpl implements TodosServices {

	@Autowired
	private TodosRepository todoRepository;
	@Override
	public List<Todo> getAllTodos() {
		// TODO Auto-generated method stub
		return todoRepository.findAll();
	}

	@Override
	public List<Todo> getTodosByUser(String user) {
		// TODO Auto-generated method stub
		return todoRepository.findByUser(user);

	}

	@Override
	public Todo getTodoById(int id) {
		Optional<Todo> todo = todoRepository.findById(id);
		
		if(todo.isPresent())
            return todo.get();
        return null;
	}

	@Override
	public Todo addTodo(Todo todo) {
		 // Save the value to the db using repository
		return todoRepository.save(todo);
	}

	@Override
	public Todo updateTodo(String name, int id, Todo todo) {
		Optional<Todo> existingTodo = todoRepository.findById(id);
        if(existingTodo.isPresent() && existingTodo.get().getUser().equals(name)){
            Todo updatedTodo = existingTodo.get();
            updatedTodo.setDescription(todo.getDescription());
            updatedTodo.setTargetDate(todo.getTargetDate());
            updatedTodo.setDone(todo.isDone());
            //save to db
            todoRepository.save(updatedTodo);
            return updatedTodo;
        }
		
		return null;
	}

	@Override
	public boolean deleteTodo(int id) {
		Optional<Todo> existingTodo = todoRepository.findById(id);
        if(existingTodo.isPresent()) {
            todoRepository.deleteById(id);
            return true;
        }    
        return false;
	}

}
