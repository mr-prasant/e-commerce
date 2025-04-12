package com.ecommerce.controller;

import com.ecommerce.entity.UserDetail;
import com.ecommerce.service.UserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user-d")
public class UserDetailControllerTest {

    private UserDetailService userDetailService;

    @GetMapping("/")
    public String health() {
        return "UserDetail server is running...";
    }

    @PostMapping("/register")
    public ResponseEntity<UserDetail> registerUserDetail(@RequestBody UserDetail userDetail) {
        return new ResponseEntity<>(
                userDetailService.registerUserDetail(userDetail),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update/{userid}")
    public ResponseEntity<UserDetail> updateUserDetail(@PathVariable String userid, @RequestBody UserDetail userDetail) {
        return new ResponseEntity<>(
                userDetailService.updateUserDetail(userid, userDetail),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{userid}")
    public ResponseEntity<String> deleteUserDetail(@PathVariable String userid) {
        userDetailService.removeUserDetail(userid);

        return new ResponseEntity<>(
                "User with userid: " + userid + " removed.",
                HttpStatus.OK
        );
    }

}
