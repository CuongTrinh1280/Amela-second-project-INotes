package com.assignment.edu.repository;

import com.assignment.edu.model.Category;
import com.assignment.edu.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INoteRepository extends JpaRepository<Note, Long> {

    Iterable<Note> findAllByCategory(Category category);

    Page<Note> findAllByTitleContaining(String title, Pageable pageable);

    Page<Note> findAllByTitleContainingOrderByIdAsc(String title, Pageable pageable);

    Page<Note> findAllByTitleContainingOrderByIdDesc(String title, Pageable pageable);

    @Query(value = "SELECT n FROM Note n WHERE n.title LIKE CONCAT('%',:keyword,'%')")
    List<Note> searchByTitleContaining(@Param("keyword") String keyword);
}
