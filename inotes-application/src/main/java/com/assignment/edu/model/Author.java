package com.assignment.edu.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long author_id;

    @Column(unique = true)
    @NotEmpty(message = "Please enter your user name!")
    @Size(min=4, max=12, message = "Your user name must between 4 and 12 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String userName;

    @NotEmpty(message = "Please enter your password!")
    @Size(min=5, message = "Your password must be more than 5 characters!")
    private String password; //codejava - user, nimda - admin

    private String role;
    private boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "author_role",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @OneToMany(targetEntity = Note.class)
    private List<Note> notes;

    public Author() {
    }

    public Author(long author_id, String userName, String password, String role) {
        this.author_id = author_id;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(long author_id) {
        this.author_id = author_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Role> getRoles() { return roles; }

    public void setRoles(List<Role> roles) { this.roles = roles; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
