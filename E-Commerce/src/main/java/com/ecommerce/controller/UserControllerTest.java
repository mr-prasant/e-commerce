package com.ecommerce.controller;

import com.ecommerce.entity.SignedInUser;
import com.ecommerce.entity.User;
import com.ecommerce.service.impl.AllUserServicesImpl;
import com.ecommerce.service.impl.SignedInUserServiceImpl;
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
    private final AllUserServicesImpl allUserServices;
    private final SignedInUserServiceImpl signedInUserService;

    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User API is running...");
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> registerUser(@RequestBody @Valid User user) {
        return new ResponseEntity<>(
                allUserServices.registerUser(user),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody @Valid User user) {
        String userid = user.getUserid();

        allUserServices.removeUser(user);
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
        if (user != null && userService.isAuthenticated(user)) {
            return new ResponseEntity<>(signedInUserService.addSignedInUser(user) != null, HttpStatus.OK);
        }

        return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/signin/valid/{userid}")
    public ResponseEntity<Boolean> signInValidity(@PathVariable String userid) {
        return new ResponseEntity<>(
                !signedInUserService.isExpired(userid),
                HttpStatus.ACCEPTED
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