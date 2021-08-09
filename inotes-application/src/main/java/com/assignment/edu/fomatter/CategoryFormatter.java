package com.assignment.edu.fomatter;

import com.assignment.edu.model.Category;
import com.assignment.edu.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

public class CategoryFormatter implements Formatter<Category> {

    private ICategoryService categoryService;

    @Autowired
    public CategoryFormatter(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public Category parse(String text, Locale locale) throws ParseException {
        Optional<Category> categoryOptional = categoryService.findById(Long.parseLong(text));
        return categoryOptional.orElse(null);
    }

    @Override
    public String print(Category object, Locale locale) {
        return new StringBuilder().append("[").append(object.getType_id()).append(", ").append(object.getType()).append("]").toString();
    }
}
