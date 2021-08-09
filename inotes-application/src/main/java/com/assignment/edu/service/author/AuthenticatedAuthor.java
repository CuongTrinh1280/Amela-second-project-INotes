package com.assignment.edu.service.author;

import com.assignment.edu.model.Author;
import com.assignment.edu.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthenticatedAuthor extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;
    private Author author;

    public AuthenticatedAuthor(Author author) {
        super(author.getUserName(), author.getPassword(), getAuthorities(author));
        this.author = author;
    }

    public Author getAuthor() {
        return author;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(Author author) {
        Set<String> roleAndPermissions = new HashSet<>();
        List<Role> roles = author.getRoles();

        roleAndPermissions.addAll(
                roles.stream().map(Role::getName).collect(Collectors.toList())
        );

        String[] roleNames = new String[roleAndPermissions.size()];
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roleAndPermissions.toArray(roleNames));
        return authorities;
    }
}
