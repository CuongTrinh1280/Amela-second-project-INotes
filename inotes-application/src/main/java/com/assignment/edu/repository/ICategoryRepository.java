package com.assignment.edu.repository;

import com.assignment.edu.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    Iterable<Category> findByType(String type);
}
