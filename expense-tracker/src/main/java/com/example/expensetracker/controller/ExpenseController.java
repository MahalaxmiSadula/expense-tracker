package com.example.expensetracker.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.ExpenseServiceImpl;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "http://localhost:4200")
public class ExpenseController {

	@Autowired
	private ExpenseServiceImpl expenseServiceImpl;

	// get all expenses
	@GetMapping("/expenses")
	public Collection<Expense> getAllExpenses() {
		return expenseServiceImpl.getListOfExpenses(10);
	}

	// get expense by id
	@GetMapping("/expenses/{id}")
	public ResponseEntity<java.util.List<Expense>> getExpenseById(@PathVariable Long id) {
		java.util.List<Expense> expenses = expenseServiceImpl.getExpense(id);
		return ResponseEntity.ok(expenses);
	}

	// add expense
	@PostMapping("/expenses")
	public Expense addExpense(@RequestBody Expense expense) {
		return expenseServiceImpl.addExpense(expense);
	}

	// update expense
	@PutMapping("/expenses")
	public ResponseEntity<Expense> updateExpense(@RequestBody Expense expenseDetails) {
		Expense expense = expenseServiceImpl.updateExpense(expenseDetails);
		return ResponseEntity.ok(expense);
	}

	// delete expense
	@DeleteMapping("/expenses/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteExpense(@PathVariable Long id) {
		Map<String, Boolean> response = new HashMap<String, Boolean>();
		response.put("Deleted", expenseServiceImpl.deleteExpense(id));
		return ResponseEntity.ok(response);
	}

}