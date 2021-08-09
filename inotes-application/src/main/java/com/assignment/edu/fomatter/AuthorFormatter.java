package com.assignment.edu.fomatter;

import com.assignment.edu.model.Author;
import com.assignment.edu.service.author.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

public class AuthorFormatter implements Formatter<Author> {

    private IAuthorService authorService;

    @Autowired
    public AuthorFormatter(IAuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    public Author parse(String text, Locale locale) throws ParseException {
        Optional<Author> authorOptional = authorService.findById(Long.parseLong(text));
        return authorOptional.orElse(null);
    }

    @Override
    public String print(Author object, Locale locale) {
        return new StringBuilder().append("[").append(object.getAuthor_id()).append(", ").append(object.getUserName()).append("]").toString();
    }
}
