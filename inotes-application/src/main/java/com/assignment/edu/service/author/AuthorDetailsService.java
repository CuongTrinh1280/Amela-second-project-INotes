package com.assignment.edu.service.author;

import com.assignment.edu.model.Author;
import com.assignment.edu.model.Role;
import com.assignment.edu.repository.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class AuthorDetailsService implements UserDetailsService {

    @Autowired
    private IAuthorRepository authorRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Author author;

        if (authorRepository.getAuthorByUsername(userName).isPresent()) {
            author = authorRepository.getAuthorByUsername(userName).get();
        } else {
            throw new UsernameNotFoundException("User name " + userName + "not found!");
        }

        return new org.springframework.security.core.userdetails.User(
                author.getUserName(), author.getPassword(), getAuthorities(author));
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(Author author) {
        String[] authorRoles = author.getRoles().stream().map(Role::getName).toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(authorRoles);
    }
}
