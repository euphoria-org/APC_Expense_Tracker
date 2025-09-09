package org.expensetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterSecurity(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) ->{
            auth.requestMatchers("/src/signup.html").permitAll();
            auth.requestMatchers(HttpMethod.POST,"/signup").permitAll();
            auth.requestMatchers("/src/css/**").permitAll();
            auth.requestMatchers("/src/images/**").permitAll();
            auth.requestMatchers("/src/js/**").permitAll();
            auth.requestMatchers("/src/js/vendor/jquery/**").permitAll();
            auth.requestMatchers("/src/output.css").permitAll();
            auth.requestMatchers("/src/about.html").permitAll();
            auth.requestMatchers("/src/js/about.js").permitAll();
            auth.anyRequest().authenticated();
//            auth.anyRequest().permitAll();
        }).formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // Specify the URL of your custom login page
                                .permitAll() // Allow access to the custom login page
                                .defaultSuccessUrl("/src/dashboard.html", true) // Redirect to /dashboard upon successful login
                                .failureUrl("/custom-login?error=true") // Redirect to /custom-login?error=true upon login failure
                                .loginProcessingUrl("/authenticate") // URL where the login form should be submitted
                                .usernameParameter("username") // Username parameter in the login form
                                .passwordParameter("password"))
                .csrf().disable();

        return http.build();
    }
}
