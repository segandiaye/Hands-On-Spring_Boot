package com.example.demo;

import org.springframework.web.context.WebApplicationContext;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.ContextConfiguration;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
//@ContextConfiguration
//@WebAppConfiguration
public class MockMvcTests {
   @Autowired
   private MockMvc mvc;
   
   @Autowired
   private WebApplicationContext context;

        //@BeforeWith
	@BeforeEach
	public void setup() {
	   mvc = MockMvcBuilders
	   .webAppContextSetup(context)
	   .apply(springSecurity())
	   .build();
	}

	@Test
	public void shouldReturnDefaultMessage() throws Exception {
	 mvc.perform(get("/login")).andDo(print()).andExpect(status().isOk());
	}

        @Test
        public void userLoginTest() throws Exception {
           mvc.perform(formLogin("/login").user("springuser").password("spring123")).andExpect(authenticated());
	}

	@Test
	public void userLoginFailed() throws Exception {
	   mvc.perform(formLogin("/login").user("springuser").password("wrongpassword")).andExpect(unauthenticated());
	}
}
