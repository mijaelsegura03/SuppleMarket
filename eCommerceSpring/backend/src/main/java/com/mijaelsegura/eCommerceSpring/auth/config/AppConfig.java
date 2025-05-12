package com.mijaelsegura.eCommerceSpring.auth.config;

import com.mijaelsegura.eCommerceSpring.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final IUserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return dni -> userRepository.findById(Long.parseLong(dni))
                .orElseThrow(() -> new UsernameNotFoundException("Not found any user with that DNI"));
    } //no hay conversión entre User y UserDetails porque User implementa UserDetails.

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    } //es el administrador de la autenticacion, provee varios metodos, y uno de
    // ellos es el de autenticar al usuario, por eso necesitamos inyectarlo en el
    //contexto de spring

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
