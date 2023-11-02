package com.topsquad.authservice.service;

import com.topsquad.authservice.entity.User;
import com.topsquad.authservice.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserDetail implements UserDetailsService {
    @Autowired
    UserRepository userRepo;

    public UserDetail(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsernameOrEmail(username, username);

        if(user == null){
            throw new UsernameNotFoundException("User not exist");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword_hash(),
                new HashSet<>());
    }

}
