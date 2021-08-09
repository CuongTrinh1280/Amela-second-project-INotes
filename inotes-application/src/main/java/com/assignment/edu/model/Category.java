package com.assignment.edu.model;

import com.assignment.edu.annotation.UniqueConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long type_id;

    @NotNull
    @UniqueConstraint(entity = Category.class)
    @Column(unique = true)
    private String type;

    @OneToMany(targetEntity = Note.class)
    private List<Note> notes;

    public Category() {
    }

    public long getType_id() {
        return type_id;
    }

    public void setType_id(long id) {
        this.type_id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
