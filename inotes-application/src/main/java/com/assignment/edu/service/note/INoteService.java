package com.assignment.edu.service.note;

import com.assignment.edu.model.Category;
import com.assignment.edu.model.Note;
import com.assignment.edu.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface INoteService extends IGeneralService<Note> {

    Iterable<Note> findAllByCategory(Category category);

    Page<Note> findAllByTitleContaining(String title, Pageable pageable);

    Page<Note> findAllByTitleContainingOrderByIdAsc(String title, Pageable pageable);

    Page<Note> findAllByTitleContainingOrderByIdDesc(String title, Pageable pageable);

    Page<Note> findAllPagingAndSorting(int pageNum, String sortField, String sortDir, Pageable pageable);

    Page<Note> findAllByTitlePagingAndSorting(String title, int pageNum, String sortField, String sortDir, Pageable pageable);

    List<Note> searchByTitleContaining(String keyword);
}
