package com.assignment.edu.service.note;

import com.assignment.edu.model.Category;
import com.assignment.edu.model.Note;
import com.assignment.edu.repository.INoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService implements INoteService {

    @Autowired
    private INoteRepository noteRepository;

    @Override
    public Iterable<Note> findAll() {
        return noteRepository.findAll();
    }

    @Override
    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    @Override
    public void save(Note note) {
        noteRepository.saveAndFlush(note);
    }

    @Override
    public void remove(Long id) {
        noteRepository.deleteById(id);
    }

    @Override
    public Iterable<Note> findAllByCategory(Category category) {
        return noteRepository.findAllByCategory(category);
    }

    @Override
    public Page<Note> findAllByTitleContaining(String title, Pageable pageable) {
        return noteRepository.findAllByTitleContaining(title, pageable);
    }

    @Override
    public Page<Note> findAllByTitleContainingOrderByIdAsc(String title, Pageable pageable) {
        return noteRepository.findAllByTitleContainingOrderByIdAsc(title, pageable);
    }

    @Override
    public Page<Note> findAllByTitleContainingOrderByIdDesc(String title, Pageable pageable) {
        return noteRepository.findAllByTitleContainingOrderByIdDesc(title, pageable);
    }

    @Override
    public Page<Note> findAllPagingAndSorting(int pageNum,
                                              String sortField,
                                              String sortDir,
                                              Pageable pageable) {
        int pageSize = 10;
        pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending()
        );

        return noteRepository.findAll(pageable);
    }

    @Override
    public Page<Note> findAllByTitlePagingAndSorting(String title,
                                                     int pageNum,
                                                     String sortField,
                                                     String sortDir,
                                                     Pageable pageable) {
        int pageSize = 10;
        pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending()
        );

        return noteRepository.findAllByTitleContaining(title, pageable);
    }

    @Override
    public List<Note> searchByTitleContaining(String keyword) {
        return noteRepository.searchByTitleContaining(keyword);
    }
}
