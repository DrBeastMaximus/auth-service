package com.topsquad.authservice.service;

import com.topsquad.authservice.entity.User;
import com.topsquad.authservice.exceptions.UsernameTakenException;
import com.topsquad.authservice.model.AuthResponse;
import com.topsquad.authservice.model.ChangePassRequest;
import com.topsquad.authservice.model.LoginDto;
import com.topsquad.authservice.model.RegisterDto;
import com.topsquad.authservice.repo.UserRepository;
import com.topsquad.authservice.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    public AuthResponse login(LoginDto loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthResponse loginRespond = new AuthResponse();
        loginRespond.setUsername(loginRequest.getUsername());
        loginRespond.setToken(jwtUtil.generateToken(loginRequest.getUsername()));
        return loginRespond;
    }

    public AuthResponse signup(RegisterDto registerRequest) throws Exception {
        try {
            // add check for username exists in a DB
            if (userRepository.existsByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail())) {
                throw new UsernameTakenException("Username or Email has already taken!");
            }

            // create user object
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword_hash(passwordEncoder.encode(registerRequest.getPassword()));
            user.setCreated_at(new Date());

            userRepository.save(user);

            AuthResponse signupRespond = new AuthResponse();
            signupRespond.setUsername(registerRequest.getUsername());
            signupRespond.setToken(jwtUtil.generateToken(registerRequest.getUsername()));

            return signupRespond;
        } catch (UsernameTakenException uex) {
            throw uex;
        } catch (Exception e){
            throw new Exception("Cannot signup this user!");
        }
    }

    public boolean tokenValidator(String token) {
        return jwtUtil.validateToken(token);
    }

    public Long getOwnerId(String token) throws Exception {
        try {
            String username = jwtUtil.getOwner(token);
            return userRepository.findByUsernameOrEmail(username, username).getId();
        } catch (Exception e){
            throw new Exception("Cannot find this user!");
        }
    }

    public AuthResponse changePass(ChangePassRequest req) throws Exception {
        // add check for username exists in a DB
        String username = jwtUtil.getOwner(req.getToken());
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (user == null) {
            throw new Exception("User not exist!");
        }

        if(!req.getNewPassword().equals(req.getConfirmPassword())){
            throw new Exception("Password and Confirmation not match!");
        }

        if(!passwordEncoder.matches(req.getOldPassword(), user.getPassword_hash())){
            throw new Exception("Old Password does not correct!");
        }

        user.setPassword_hash(passwordEncoder.encode(req.getNewPassword()));

        userRepository.save(user);

        AuthResponse signupRespond = new AuthResponse();
        signupRespond.setUsername(user.getUsername());
        signupRespond.setToken(jwtUtil.generateToken(user.getUsername()));

        return signupRespond;
    }
}
