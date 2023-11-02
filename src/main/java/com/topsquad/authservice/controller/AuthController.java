package com.topsquad.authservice.controller;

import com.topsquad.authservice.model.ChangePassRequest;
import com.topsquad.authservice.model.LoginDto;
import com.topsquad.authservice.model.RegisterDto;
import com.topsquad.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginReq){
        try {
            return new ResponseEntity<>(authService.login(loginReq), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Incorrect username or password!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerReq) {
        try {
            return new ResponseEntity<>(authService.signup(registerReq), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/whosOwner")
    public ResponseEntity<?> getTokenOwnerID(@RequestParam String token) {
        try {
            return new ResponseEntity<>(authService.getOwnerId(token).toString(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/valid")
    public ResponseEntity<?> checkToken(@RequestParam String token){
        return new ResponseEntity<>(authService.tokenValidator(token), HttpStatus.OK);
    }

    @PostMapping("/changePass")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassRequest request) {
        try {
            return new ResponseEntity<>(authService.changePass(request), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}