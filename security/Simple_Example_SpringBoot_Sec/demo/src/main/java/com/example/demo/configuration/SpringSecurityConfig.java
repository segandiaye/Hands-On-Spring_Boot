package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
//@ComponentScan(basePackages = { "com.example.demo.*" })
@PropertySource("classpath:client.properties")
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    //@Value("${name}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.github.redirectUri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.github.authorizationUri}")
    private String authorizationUri;
    @Value("${spring.security.oauth2.client.registration.github.tokenUri}")
    private String tokenUri;
    private String client_name;
   
   @Autowired
   public SpringSecurityConfig(@Value("${name}") String clientId){
     this.clientId = clientId;
    System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii+++++++++ " + clientId);
   }

    @PostConstruct
    public void init() {
        this.clientId = "eeeeeeeeee";
        System.out.println("================== " + clientId + " ================== ");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("------------------ " + clientId + " --------------- ");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("cccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
        auth.inMemoryAuthentication()
            .withUser("springuser").password(passwordEncoder().encode("spring123"))
            .roles("USER")
            .and()
            .withUser("springadmin").password(passwordEncoder().encode("admin123"))
            .roles("ADMIN", "USER");
    }
    
    @Override 
    public void configure(HttpSecurity http) throws Exception {
        http
	    .authorizeRequests()
		    .antMatchers("/admin").hasRole("ADMIN")
		    .antMatchers("/user").hasRole("USER")
                    .antMatchers("/ping").permitAll()
		    .anyRequest().authenticated()
                    .and()
            .formLogin()
                    //.loginPage("/login")
		    //.permitAll()
		    .and()
            .oauth2Login();
	    //.logout()
            	//.logoutUrl("/logout")
            //.logoutSuccessUrl("/login");
    }
    
    	@Bean
        public ClientRegistrationRepository clientRegistrationRepository() {
               System.out.println("ooooooooooooooooooooooo " + "11111" + " ooooooooooooooooooooooo");
                return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
        }
        
        private ClientRegistration googleClientRegistration() {
                System.out.println("ooooooooooooooooooooooo " + clientId + " ooooooooooooooooooooooo");
                return ClientRegistration.withRegistrationId("github")
                        .clientId(clientId)
                        .clientSecret("secret")
                        //.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUri("http://localhost:8090/login/oauth2/code/github")
                        //.scope("openid", "profile", "email", "address", "phone")
                        .authorizationUri("https://github.com/login/oauth/authorize")
                        .tokenUri("https://github.com/login/oauth/access_token")
                        //.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                        //.userNameAttributeName(IdTokenClaimNames.SUB)
                        //.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                        //.clientName("Google")
                        .build();
        }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println("+++++++++++++++++++++++ " + clientId + " +++++++++++++++++++++ ");
        return new BCryptPasswordEncoder();
    }
/*
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
      System.out.println("+++++++++++++++++++++++ok " + "static" + " +++++++++++++++++++++ ");
      return new PropertySourcesPlaceholderConfigurer();
    }*/

    @Bean
    public static String sTat() {
      System.out.println("+++++++++++++++++++++++ok " + "staticStat" + " +++++++++++++++++++++ ");
      return "Stat";
    }
}

