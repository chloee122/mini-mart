package com.ecommerce.minimart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.ecommerce.minimart.service.UserDetailServiceImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
        @Autowired
        private UserDetailServiceImpl userDetailsService;

        @Bean
        public SecurityFilterChain configure(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/css/**", "/h2-console/**", "/signup").permitAll()
                                .requestMatchers("/categories/**", "/products/add",
                                                "/products/edit/**", "/products/delete/**")
                                .hasAuthority("ADMIN").anyRequest().authenticated())
                                .headers(headers -> headers.frameOptions(
                                                frameOptions -> frameOptions.disable())) // For
                                                                                         // h2-console
                                .formLogin(formLogin -> formLogin.loginPage("/login")
                                                .defaultSuccessUrl("/products", true).permitAll())
                                .logout(logout -> logout.permitAll()).csrf(csrf -> csrf.disable());;
                return http.build();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userDetailsService)
                                .passwordEncoder(new BCryptPasswordEncoder());
        }
}
