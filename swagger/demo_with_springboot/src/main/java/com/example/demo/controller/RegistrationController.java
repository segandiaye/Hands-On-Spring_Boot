package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entities.User;

@RestController
public class RegistrationController{

	 @PostMapping("/registration")
	 public String register(@RequestBody User user) {
	  return "Hello " + user.getName();
	 }

}
