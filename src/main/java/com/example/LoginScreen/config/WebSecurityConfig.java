package com.example.LoginScreen.config;

import com.example.LoginScreen.user.User;
import com.example.LoginScreen.user.UserDetailsServiceImpl;
import com.example.LoginScreen.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user1").password(passwordEncoder().encode("user")).roles("USER");
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public CommandLineRunner initUser(UserRepository repository) {
        return (args) -> {
            repository.save(new User("user", passwordEncoder().encode("user"), "USER"));
            repository.save(new User("user2", passwordEncoder().encode("user"), "USER" ));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/").permitAll()
                .antMatchers("/login*").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/Users").permitAll()
                .antMatchers("/process_register").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("user")
                    .loginProcessingUrl("/doLogin")
                    .successForwardUrl("/loginSuccessHandler")
                    .failureUrl("/login?error=true")
                    .permitAll()
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .permitAll()
                .and().rememberMe().key("somesecuritykey3421323425");
                    //.tokenRepository(persistentTokenRepository());

        http.csrf().disable();
    }
}
