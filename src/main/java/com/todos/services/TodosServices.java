package com.todos.services;

import java.util.List;

import com.todos.models.Todo;

public interface TodosServices {

	public List<Todo> getAllTodos();
	public List<Todo> getTodosByUser(String user);
	
	Todo getTodoById(int id);
	
	Todo addTodo(Todo todo);
	
	Todo updateTodo(String name, int id , Todo todo);
	boolean deleteTodo(int id);
}
