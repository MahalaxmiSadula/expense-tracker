package com.example.expensetracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.expensetracker.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	@Query(value = """
			select t from Expense t inner join User u\s
			on t.user.id = u.id\s
			where u.id = :id\s
			""")
	List<Expense> findByUser(Long id);

}