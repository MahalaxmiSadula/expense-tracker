package com.example.expensetracker.service;

import java.util.Collection;
import java.util.List;

import com.example.expensetracker.model.Expense;

public interface ExpenseService {

	Expense addExpense(Expense expense);

	Collection<Expense> getListOfExpenses(int limit);

	List<Expense> getExpense(Long id);

	Expense updateExpense(Expense emp);

	Boolean deleteExpense(Long id);
}