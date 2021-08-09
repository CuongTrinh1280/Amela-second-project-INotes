package com.assignment.edu.controller;

import com.assignment.edu.model.Category;
import com.assignment.edu.model.Note;

import com.assignment.edu.service.category.ICategoryService;
import com.assignment.edu.service.note.INoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class NoteController {

    @Autowired
    private INoteService noteService;

    @Autowired
    private ICategoryService categoryService;

    @ModelAttribute("categories")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @RequestMapping(value = {"/notes/{pageNum}"}, method = RequestMethod.GET)
    public ModelAndView showListNotesWithPagingAndSorting(@PathVariable("pageNum") int pageNum,
                                                          @Param("sortField") String sortField,
                                                          @Param("sortDir") String sortDir,
                                                          Pageable pageable) {
        Page<Note> notes;
        ModelAndView modelAndView;

        modelAndView = new ModelAndView("/note/list");
        notes = noteService.findAllPagingAndSorting(pageNum, sortField, sortDir, pageable);

        return getModelAndView(pageNum, sortField, sortDir, notes, modelAndView);
    }

    @RequestMapping(value = "/notes/{pageNum}/search", method = RequestMethod.GET)
    public ModelAndView showSearchListWithPagingAndSorting(@PathVariable("pageNum") int pageNum,
                                                           @RequestParam(value = "title") Optional<String> keyword,
                                                           @Param("sortField") String sortField,
                                                           @Param("sortDir") String sortDir,
                                                           Pageable pageable) {

        Page<Note> notes;
        ModelAndView modelAndView;

        if (keyword.isPresent()) {
            notes = noteService.findAllByTitlePagingAndSorting(keyword.get(), pageNum, sortField, sortDir, pageable);

            modelAndView = new ModelAndView("/note/result");
            modelAndView.addObject("title", keyword.get());
            modelAndView.addObject("messageSearch", "Search result for keyword: " + keyword.get());
        } else {
            notes = noteService.findAllPagingAndSorting(pageNum, sortField, sortDir, pageable);

            modelAndView = new ModelAndView("/note/list");
        }

        return getModelAndView(pageNum, sortField, sortDir, notes, modelAndView);
    }

    private ModelAndView getModelAndView(@PathVariable("pageNum") int pageNum,
                                         @Param("sortField") String sortField,
                                         @Param("sortDir") String sortDir,
                                         Page<Note> notes,
                                         ModelAndView modelAndView) {

        modelAndView.addObject("currentPage", pageNum);
        modelAndView.addObject("totalPages", notes.getTotalPages());
        modelAndView.addObject("totalItems", notes.getTotalElements());

        modelAndView.addObject("notes", notes);

        modelAndView.addObject("sortField", sortField);
        modelAndView.addObject("sortDir", sortDir);
        modelAndView.addObject("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return modelAndView;
    }

    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    public String handleFileUpload(
            @NotNull @RequestParam("files") CommonsMultipartFile[] fileUpload) throws Exception {

        for (CommonsMultipartFile file : fileUpload) {
            file.transferTo(new File(Objects.requireNonNull(file.getOriginalFilename())));
        }
        return "/note/list";
    }

    @RequestMapping(value = "/create-note", method = RequestMethod.GET)
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/note/create");
        modelAndView.addObject("note", new Note());

        return modelAndView;
    }

    @RequestMapping(value = "/create-note", method = RequestMethod.POST)
    public ModelAndView createNote(@ModelAttribute("note") Note note) {
        noteService.save(note);

        ModelAndView modelAndView = new ModelAndView("/note/create");
        modelAndView.addObject("note", new Note());
        modelAndView.addObject("messageCreate", "Note created successfully!");

        return modelAndView;
    }

    @RequestMapping(value = "/edit-note/{id}", method = RequestMethod.GET)
    public ModelAndView showEditForm(@PathVariable Long id) {
        var note = noteService.findById(id);
        ModelAndView modelAndView;

        if (note.isPresent()) {
            modelAndView = new ModelAndView("/note/edit");
            modelAndView.addObject("note", note.get());
        } else {
            modelAndView = new ModelAndView("/error-404");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/edit-note", method = RequestMethod.POST)
    public ModelAndView updateNote(@ModelAttribute("note") Note note) {
        noteService.save(note);

        ModelAndView modelAndView = new ModelAndView("/note/edit");
        modelAndView.addObject("note", note);
        modelAndView.addObject("messageUpdate", "Note updated successfully!");

        return modelAndView;
    }

    @RequestMapping(value = "/delete-note/{id}", method = RequestMethod.GET)
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Optional<Note> note = noteService.findById(id);
        ModelAndView modelAndView;

        if (note.isPresent()) {
            modelAndView = new ModelAndView("/note/delete");
            modelAndView.addObject("note", note.get());
        } else {
            modelAndView = new ModelAndView("/error-404");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/delete-note", method = RequestMethod.POST)
    public String deleteNote(@ModelAttribute("note") Note note) {
        noteService.remove(note.getId());
        long totalItems = noteService.findAll().spliterator().getExactSizeIfKnown();

        final int pageSize = 10;
        int totalPages = (int) (totalItems / pageSize + 1);
        int page = (int) (note.getId() / pageSize + 1);
        int currentPage = Math.min(totalPages, page);

        String currentPos = Integer.toString(currentPage);
        return new StringBuilder().append("redirect:/home/notes/").append(currentPos).append("?sortField=id&sortDir=asc").toString();
    }

    @RequestMapping(value = "/view-note/{id}", method = RequestMethod.GET)
    public ModelAndView viewCategory(@PathVariable("id") Long id){
        Optional<Note> noteOptional = noteService.findById(id);

        if (noteOptional.isEmpty()){
            return new ModelAndView("/error-404");
        }

        ModelAndView modelAndView = new ModelAndView("/note/view");
        modelAndView.addObject("note", noteOptional.get());

        return modelAndView;
    }
}
