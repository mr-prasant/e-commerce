package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ECommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);


//		UserService userService = container.getBean(UserServiceImpl.class);
//		System.out.println("running main: " + (userService == null));
//
//		User user = new User();
//		user.setUserid("p@mail.com");
//		user.setPassword("p@123");
//		user.setRoles("admin");
//
//		System.out.println(userService.registerUser(user));
	}

}
