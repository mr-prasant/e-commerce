package com.ecommerce.controller;

import com.ecommerce.entity.User;
import com.ecommerce.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserControllerTest {

    private final UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User API is running...");
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid User user) {
        User savedUser = userService.registerUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody @Valid User user) {
        String userid = user.getUserid();

        userService.removeUser(user);
        return new ResponseEntity<>(
                "user deleted: " + userid,
                HttpStatus.OK
        );
    }

    @PutMapping("/update/{userid}")
    public ResponseEntity<User> updateUser(@PathVariable String userid, @RequestBody User user) {
        return new ResponseEntity<>(
                userService.updateUser(userid, user),
                HttpStatus.OK
        );
    }

    @GetMapping("/signin")
    public ResponseEntity<Boolean> signIn(@RequestBody @Valid User user) {
        return new ResponseEntity<>(
                userService.isAuthenticated(user),
                HttpStatus.OK
        );
    }

    @GetMapping("/{userid}")
    public ResponseEntity<User> getUser(@PathVariable String userid) {
        return new ResponseEntity<>(
                userService.getUser(userid),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(
                userService.getAllUsers(),
                HttpStatus.OK
        );
    }
}