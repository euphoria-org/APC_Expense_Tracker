package org.expensetracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

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
            auth.requestMatchers("/src/admin.html").hasRole("ADMIN");
            auth.requestMatchers("/admin/**").hasRole("ADMIN");
            auth.requestMatchers("/api/admin/**").hasRole("ADMIN");
            auth.requestMatchers("/src/dashboard.html").hasAnyRole("USER", "ADMIN");
            auth.anyRequest().authenticated();
//            auth.anyRequest().permitAll();
        }).formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // Specify the URL of your custom login page
                                .permitAll() // Allow access to the custom login page
                                .successHandler(customAuthenticationSuccessHandler) // Use custom success handler for role-based redirection
                                .failureUrl("/login?error=true") // Redirect to /login?error=true upon login failure
                                .loginProcessingUrl("/authenticate") // URL where the login form should be submitted
                                .usernameParameter("username") // Username parameter in the login form
                                .passwordParameter("password"))
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout=true")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID"))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
