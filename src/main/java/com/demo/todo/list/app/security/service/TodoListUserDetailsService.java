package com.demo.todo.list.app.security.service;

import com.demo.todo.list.app.model.security.TodoListUserDetails;
import com.demo.todo.list.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoListUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(TodoListUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
