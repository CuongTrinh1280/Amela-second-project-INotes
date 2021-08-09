package com.assignment.edu.service.author;

import com.assignment.edu.model.Author;
import com.assignment.edu.repository.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService implements IAuthorService {

    @Autowired
    private IAuthorRepository authorRepository;

    @Override
    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public void save(Author author) {
        authorRepository.saveAndFlush(author);
    }

    @Override
    public void remove(Long id) {
        authorRepository.deleteById(id);
    }
}
