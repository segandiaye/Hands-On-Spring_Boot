package com.example.demo.controller;

import javax.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import java.security.Principal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;


import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

//@RestController
@Controller
public class LoginController{
   
   @Autowired
   private final  OAuth2AuthorizedClientService authorizedClientService;
    
   public LoginController(OAuth2AuthorizedClientService authorizedClientService) {
   	this.authorizedClientService = authorizedClientService;
   }

   @GetMapping("/ping")
   //@ResponseBody
   public String ping(Model model) {
      model.addAttribute("name", "Ndiaye");
      return "ping2";
   }

   @RolesAllowed("USER")
   @RequestMapping("/**")
   @ResponseBody
   public String getUser()
   {
      return "Welcome User";
   }

   @RolesAllowed({"USER","ADMIN"})
   @RequestMapping("/admin")
   @ResponseBody
   public String getAdmin()
   {
      return "Welcome Admin";
   }
   
   /*
   @RequestMapping("/ping")
   public ModelAndView ping() {
      ModelAndView modelAndView = new ModelAndView();
      modelAndView.setViewName("ping");
   
      return modelAndView;
    }*/

   @RequestMapping("/user")
   @ResponseBody
   public String getUserInfo(Principal user) {
      StringBuffer userInfo= new StringBuffer();
      if(user instanceof UsernamePasswordAuthenticationToken){
         userInfo.append(getUsernamePasswordLoginInfo(user));
      }else if(user instanceof OAuth2AuthenticationToken){
         userInfo.append(getOauth2LoginInfo(user));
      }

      return userInfo.toString();
    }

   private StringBuffer getUsernamePasswordLoginInfo(Principal user)
   {
      StringBuffer usernameInfo = new StringBuffer();
      
      UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) user);
      if(token.isAuthenticated()){
         //user u = (user) token.getPrincipal();
         // token.getUsername()
         usernameInfo.append("Welcome, " + user.getName());
      }
      else{
         usernameInfo.append("NA");
      }
      return usernameInfo;
   }

   private StringBuffer getOauth2LoginInfo(Principal user){

	   StringBuffer protectedInfo = new StringBuffer();
	   
	   OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
	   OAuth2AuthorizedClient authClient = this.authorizedClientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(),authToken.getName());

	   if(authToken.isAuthenticated()){
		   Map<String,Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();
		   
		   String userToken = authClient.getAccessToken().getTokenValue();
		   protectedInfo.append("Welcome, " + userAttributes.get("name")+"<br><br>");
		   protectedInfo.append("e-mail: " + userAttributes.get("email")+"<br><br>");
		   protectedInfo.append("Access Token: " + userToken+"<br><br>");
	   }
	   else{
	   	protectedInfo.append("NA");
	   }
	return protectedInfo;
   }

/*
   @RequestMapping("/*")
   public String getGithub()
   {
     return "Welcome Github user!";
   }*/
}
