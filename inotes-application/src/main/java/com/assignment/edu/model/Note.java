package com.assignment.edu.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "note")
public class Note {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String title;
    private String content;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public Note() {
    }

    public Note(String title, String content, Category category, LocalDate date, Author author) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.date = date;
        this.author = author;
    }

    public Note(long id, String title, String content, Category category, LocalDate date, Author author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.date = date;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", category=" + category +
                ", author=" + author +
                '}';
    }
}
