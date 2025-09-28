package org.expensetracker.service;

import org.expensetracker.model.users;
import org.expensetracker.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetailService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        users user = userRepository.findByUsername(username);
        if(user==null) throw new UsernameNotFoundException("No user with username "+username);
        
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );
        
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

    public void addUser(String username , String password) {
        users user = userRepository.findByUsername(username);
        if(user==null){
            String password_hash = passwordEncoder.encode(password);
            userRepository.addNewUser(username,password_hash, "USER");
        }
    }
}
