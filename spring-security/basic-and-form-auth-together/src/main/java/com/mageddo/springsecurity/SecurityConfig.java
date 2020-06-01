package com.mageddo.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public UserDetailsService userDetailsService(final PasswordEncoder encoder) {
    final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(
        User
            .withUsername("admin")
            .password(encoder.encode("secret"))
            .roles("ADMIN", "API_USER")
            .build()
    );
    return manager;
  }

  @Bean
  PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }


  @Configuration
  @Order(1)
  public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
          .antMatcher("/api/**")
          .authorizeRequests()
          .anyRequest().hasRole("API_USER")
          .and()
          .httpBasic()
//				.and()
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      ;
    }
  }

  @Configuration
  @Order(2)
  public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable()
          .authorizeRequests()
          .antMatchers("/secret-area/**").hasRole("ADMIN")
          .and()
          .formLogin()
          .and()
          .logout().permitAll()
      ;
    }
  }

}
