package com.todos.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.todos.models.Todo;
import com.todos.services.TodosServices;

//@Service
public class TodoServiceInMemoryStorageImpl implements TodosServices {

	private static List<Todo> todos = new ArrayList<>();
	private static int todosCount = 3;
	
	static {
//        todos.add(new Todo(1, "Jack", "Learn Spring Boot", new Date(), false));
//        todos.add(new Todo(2, "Jack", "Learn Spring JPA", new Date(), false));
//        todos.add(new Todo(3, "Jill", "Climb the hill", new Date(), false));
    }

	
	
	public List<Todo> getAllTodos() {
		// TODO Auto-generated method stub
		return todos;
	}



	@Override
	public List<Todo> getTodosByUser(String user) {
		// TODO Auto-generated method stub
		List<Todo> userTodos = new ArrayList<>();
		for(Todo todo: todos) {
			if(todo.getUser().equals(user)) {
				userTodos.add(todo);
			}
		}
		
		return userTodos;
	}
	
	public Todo getTodoById(int id) {

		for (Todo todo : todos) {

			if (todo.getId() == id) {
				return todo;
			}
		}
		return null;
	}



	@Override
	public Todo addTodo(Todo todo) {
		todo.setId(++todosCount);
		todos.add(todo);
		return todo;
	}



	@Override
	public Todo updateTodo(String name, int id, Todo todo) {
		Todo existingTodo = this.getTodoById(id);
		if(existingTodo != null && name.equals(existingTodo.getUser())) {
			existingTodo.setDescription(todo.getDescription());
			existingTodo.setTargetDate(todo.getTargetDate());
			existingTodo.setDone(todo.isDone());
			
			return existingTodo;
		} else {
			return null;
		}
	}



	@Override
    public boolean deleteTodo(int id) {
        for(int i =0 ; i< todos.size(); i++) {
            if(todos.get(i).getId()== id) {
                todos.remove(i);
                return true;
            }
        }
        return false;    
    }

}
