package com.maids.cc.Library_Management_System.config;

import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.UserNotFoundException;
import com.maids.cc.Library_Management_System.model.Librarian;
import com.maids.cc.Library_Management_System.repository.LibrarianRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibrarianUserDetails implements UserDetailsService {

    private final LibrarianRepository librarianRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Librarian librarian = librarianRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User wasn't found"));
        String userName = librarian.getUserName();
        String password = librarian.getPassword();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(librarian.getRole()));
        return new User(userName, password, authorities);
    }

}
