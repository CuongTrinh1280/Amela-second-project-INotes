package com.assignment.edu.controller;

import com.assignment.edu.model.Category;
import com.assignment.edu.model.Note;
import com.assignment.edu.service.category.ICategoryService;
import com.assignment.edu.service.note.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/home")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private INoteService noteService;

    public CategoryController() {
    }

    @RequestMapping(value = "/view-category/{id}", method = RequestMethod.GET)
    public ModelAndView viewCategory(@PathVariable("id") Long id){
        Optional<Category> categoryOptional = categoryService.findById(id);

        if(categoryOptional.isEmpty()){
            return new ModelAndView("/error-404");
        }

        Iterable<Note> notes = noteService.findAllByCategory(categoryOptional.get());

        ModelAndView modelAndView = new ModelAndView("/category/view");
        modelAndView.addObject("category", categoryOptional.get());
        modelAndView.addObject("notes", notes);
        return modelAndView;
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ModelAndView listCategories() {
        Iterable<Category> categories = categoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("/category/list");
        modelAndView.addObject("categories", categories);

        return modelAndView;
    }

    @RequestMapping(value = "/create-category", method = RequestMethod.GET)
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/category/create");
        modelAndView.addObject("category", new Category());

        return modelAndView;
    }

    @RequestMapping(value = "/create-category", method = RequestMethod.POST)
    public ModelAndView saveCategory(@ModelAttribute("category") Category category) {
        categoryService.save(category);

        ModelAndView modelAndView = new ModelAndView("/category/create");
        modelAndView.addObject("category", new Category());
        modelAndView.addObject("message", "New category created successfully");

        return modelAndView;
    }

    @RequestMapping(value = "/edit-category/{id}", method = RequestMethod.GET)
    public ModelAndView showEditForm(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        ModelAndView modelAndView;

        if (category.isPresent()) {
            modelAndView = new ModelAndView("/category/edit");
            modelAndView.addObject("category", category.get());
        } else {
            modelAndView = new ModelAndView("/error-404");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/edit-category", method = RequestMethod.POST)
    public ModelAndView updateCategory(@ModelAttribute("category") Category category) {
        categoryService.save(category);

        ModelAndView modelAndView = new ModelAndView("/category/edit");
        modelAndView.addObject("category", category);
        modelAndView.addObject("message", "Category updated successfully");

        return modelAndView;
    }

    @RequestMapping(value = "/delete-category/{id}", method = RequestMethod.GET)
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        ModelAndView modelAndView;

        if (category.isPresent()) {
            modelAndView = new ModelAndView("/category/delete");
            modelAndView.addObject("category", category.get());
        } else {
            modelAndView = new ModelAndView("/error-404");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/delete-category", method = RequestMethod.POST)
    public String deleteCategory(@ModelAttribute("category") Category category) {
        categoryService.remove(category.getType_id());
        return "redirect:/home/categories";
    }
}
