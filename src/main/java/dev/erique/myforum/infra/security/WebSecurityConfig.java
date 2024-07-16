package dev.erique.myforum.infra.security;

import dev.erique.myforum.infra.security.jwt.SecurityFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

    private static final String ADMIN = "ADMIN";
    private static final String CLIENT = "CLIENT";

    final SecurityFilter securityFilter;

    public WebSecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( authorize -> authorize
                        .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/public/loginAdmin").permitAll()
                        .requestMatchers("/public/loginClient").permitAll()

                        .requestMatchers("/client/teste").permitAll()
                        .requestMatchers("/client/saveNew").permitAll()
                        .requestMatchers("/client/getOne/{clientId}").hasRole(ADMIN)
                        .requestMatchers("/client/deleteOne/{clientId}").hasRole(ADMIN)
                        .requestMatchers("/client/getAll/{page}").hasRole(ADMIN)


                        .requestMatchers("/topic/saveNew").hasRole(CLIENT)
                        .requestMatchers("/topic/getOne/{topicId}").permitAll()
                        .requestMatchers("/topic/getAll/{page}").permitAll()
                        .requestMatchers("/topic/getAll/{topicId}/{page}").permitAll()
                        .requestMatchers("/topic/deleteOne/{topictId}").hasRole(CLIENT)

                        .requestMatchers("/answer/saveNew{answerId}").hasRole(CLIENT)
                        .requestMatchers("/answer/getOne/{answerId}").permitAll()
                        .requestMatchers("/answer/getAll/{page}").hasRole(CLIENT)
                        .requestMatchers("/answer/deleteOne/{answerId}").hasRole(CLIENT)


                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public FilterRegistrationBean corsFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean(new CORSFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

}
