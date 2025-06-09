package com.andreafueyo.TFCesteticien_AndreaGomezFueyo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
public class SecurityConfig {

	private final AuthenticationSuccessHandler customSuccessHandler;
	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsService;

	@Autowired
	public SecurityConfig(AuthenticationSuccessHandler customSuccessHandler) {
		this.customSuccessHandler = customSuccessHandler;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/verservicios", "/registrarcliente","/iniciarsesion", "/quienessomos", "/register", "/css/**", "/js/**", "/imagenes/**", "/static/**").permitAll() 
				.requestMatchers("/menuadmin/").hasRole("ADMIN")
				.requestMatchers("/menupersonal/").hasRole("PERSONAL")
				.requestMatchers("/menucliente/").hasRole("CLIENTE")
				.anyRequest().authenticated()
				)
		.formLogin(form -> form
				.loginPage("/iniciarsesion") 
				.loginProcessingUrl("/login") 
				.successHandler(customSuccessHandler) 
				.failureUrl("/iniciarsesion?error=true") 
				.permitAll() 
				)
		.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/iniciarsesion?logout")  
				.invalidateHttpSession(true) 
				.deleteCookies("JSESSIONID")
				.permitAll()
				)
		.sessionManagement(session -> session
			    .invalidSessionUrl("/iniciarsesion?sessionExpired=true") 
			    .maximumSessions(1)
			    .maxSessionsPreventsLogin(false)
			    .expiredUrl("/iniciarsesion?sessionExpired=true") 
				);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(customUserDetailsService)
				.passwordEncoder(passwordEncoder()) 
				.and()
				.build();
	}
}