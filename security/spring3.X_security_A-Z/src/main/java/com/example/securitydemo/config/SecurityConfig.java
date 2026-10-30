package com.example.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import com.example.securitydemo.security.CustomUserDetailsService;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {	

    // 1. Configuration des autorisations HTTP
        @Bean
	public SecurityFilterChain securityFilterChain(
		HttpSecurity http
		) throws Exception {

	    return http
		//.csrf(csrf -> csrf.ignoringRequestMatchers("/api/auth/login") )
		.csrf(csrf -> csrf.disable())
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )

            .authorizeHttpRequests(auth -> auth

                    .requestMatchers(HttpMethod.GET, "/api/public").permitAll()

		    .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login", "/api/auth/refresh", "/api/auth/logout").permitAll()

		    .requestMatchers(HttpMethod.GET, "/api/me").authenticated()

                    .requestMatchers(HttpMethod.GET, "/api/user").hasAnyRole("USER", "ADMIN")

                    .requestMatchers(HttpMethod.GET, "/api/manager", "/api/manager/report").hasAnyRole("MANAGER", "ADMIN")

                    .requestMatchers(HttpMethod.GET, "/api/admin", "/api/admin/data").hasRole("ADMIN")

                    .anyRequest().denyAll()
            )

            .oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwt ->
                            jwt.jwtAuthenticationConverter(
                                    jwtAuthenticationConverter()
                            )
                    )
            )

            .build();
	}

    // 2. Encodage des mots de passe
        @Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

    // 3. Utilisateurs de démonstration
    //@Bean
    //public UserDetailsService userDetailsService(
    //	PasswordEncoder passwordEncoder
    //	) {
    //  UserDetails user = User.builder()
    //	.username("user")
    //	.password(passwordEncoder.encode("user123"))
    //	.roles("USER")
    //	.build();
    //  UserDetails admin = User.builder()
    //	.username("admin")
    //	.password(passwordEncoder.encode("admin123"))
    //	.roles("ADMIN")
    //	.build();
    //  return new InMemoryUserDetailsManager(user, admin);
    //}

    //@Bean
    //public DaoAuthenticationProvider authenticationProvider(
    //	UserDetailsService userDetailsService,
    //	PasswordEncoder passwordEncoder
    //	) {
    //  DaoAuthenticationProvider provider =
    //	new DaoAuthenticationProvider(userDetailsService);
    //  provider.setPasswordEncoder(passwordEncoder);
    //return provider;
    //	}

    //@Bean
    //public DaoAuthenticationProvider authenticationProvider(
    //      UserDetailsService userDetailsService,
    //    PasswordEncoder passwordEncoder
    //  ) {
    // DaoAuthenticationProvider provider =
    //   new DaoAuthenticationProvider(userDetailsService);
    //provider.setPasswordEncoder(passwordEncoder);
    //return provider;
    //}
    

    	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {

    		JwtGrantedAuthoritiesConverter authoritiesConverter =
            		new JwtGrantedAuthoritiesConverter();

    		authoritiesConverter.setAuthoritiesClaimName("scope");

    		authoritiesConverter.setAuthorityPrefix("");

    		JwtAuthenticationConverter converter =
            		new JwtAuthenticationConverter();

    		converter.setJwtGrantedAuthoritiesConverter(
            		authoritiesConverter
    		);

    		return converter;
	}	

        @Bean
	public DaoAuthenticationProvider authenticationProvider(
		CustomUserDetailsService userDetailsService,
		PasswordEncoder passwordEncoder
		) {

	    DaoAuthenticationProvider provider =
		new DaoAuthenticationProvider(userDetailsService);

	    provider.setPasswordEncoder(passwordEncoder);

	    return provider;
	}


        @Bean
	public AuthenticationManager authenticationManager(
		DaoAuthenticationProvider authenticationProvider
		) {

	    return new ProviderManager(authenticationProvider);
	}
}
