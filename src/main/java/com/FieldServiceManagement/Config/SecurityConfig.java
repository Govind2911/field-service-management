package com.FieldServiceManagement.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.FieldServiceManagement.Security.CustomUserDetailsService;
import com.FieldServiceManagement.Security.JWTUtil;
import com.FieldServiceManagement.Security.JwtAuthenticationFilter;
import com.FieldServiceManagement.Security.TokenBlockService;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JWTUtil jwtUtil;
	
    @Autowired
    private CustomUserDetailsService customeUserDetailsService;
    
    @Autowired
    private TokenBlockService tokenBlockService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    	return config.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwoordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
  
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(
                jwtUtil,
                customeUserDetailsService,
                tokenBlockService
        );
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    	
    	http.csrf(csrf-> csrf.disable())
    	.sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    	.authorizeHttpRequests(auth->auth.requestMatchers("/api/user_auth/**","/api/email_log/**")
    			                         .permitAll()
    			                         .anyRequest()
    			                         .authenticated())
    	 .exceptionHandling(exception -> exception
    	            .authenticationEntryPoint((request, response, ex) -> {
    	                System.err.println(
    	                    "401 Authentication failed: "
    	                    + request.getRequestURI()
    	                    + " | " + ex.getMessage()
    	                );

    	                response.setStatus(401);
    	                response.setContentType("application/json");
    	                response.getWriter().write(
    	                    "{\"message\":\"Authentication required or token invalid\"}"
    	                );
    	            })
    	            .accessDeniedHandler((request, response, ex) -> {
    	                System.err.println(
    	                    "403 Access denied: "
    	                    + request.getRequestURI()
    	                    + " | " + ex.getMessage()
    	                );

    	                response.setStatus(403);
    	                response.setContentType("application/json");
    	                response.getWriter().write(
    	                    "{\"message\":\"Access denied\",\"path\":\""
    	                    + request.getRequestURI() + "\"}"
    	                );
    	            })
    	        );
    	
    	
    	http.addFilterBefore(
    	        jwtAuthenticationFilter(),
    	        UsernamePasswordAuthenticationFilter.class
    	);

    	return http.build();
    }
    
}