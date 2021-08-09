package com.assignment.edu.repository;

import com.assignment.edu.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a WHERE a.userName = :userName")
    Optional<Author> getAuthorByUsername(@Param("userName") String userName);

    Author findByUserName(String userName);
}
