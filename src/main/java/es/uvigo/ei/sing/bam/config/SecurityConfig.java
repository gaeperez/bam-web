package es.uvigo.ei.sing.bam.config;

import es.uvigo.ei.sing.bam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    // @formatter:off
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/signup").permitAll()
                    .antMatchers("/agent-type/**").hasRole("ADMIN")
                    .antMatchers("/event-type/**").hasRole("ADMIN")
                    .antMatchers("/shape/**").hasRole("ADMIN")
                    .anyRequest()
                    .authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/index")
                    .permitAll()
                .and()
                    .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
                "/resources/static/**", "/css/**", "/js/**", "/img/**", "/fonts/**",
                "/images/**", "/scss/**", "/vendor/**", "/favicon.ico", "/favicon.png",
                "/v2/api-docs", "/configuration/ui", "/configuration/security", "/webjars/**", "/actuator",
                "/actuator/**");
    }
}
