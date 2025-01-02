package com.labmanager.labmanager.config;

import com.labmanager.labmanager.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioService usuarioService;

    // Usando @Lazy para resolver a dependência circular
    public SecurityConfig(@Lazy UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Definir o PasswordEncoder (BCryptPasswordEncoder)
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    // Define o handler para falhas de autenticação
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
    }

    // Configuração de segurança para a aplicação
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/public/**", "/css/**", "/js/**", "/images/**").permitAll()  // Endpoints públicos
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Restringir para usuários com a role "ADMIN"
                        .requestMatchers("/funcionario/**").hasRole("FUNCIONARIO")  // Restringir para usuários com a role "FUNCIONARIO"
                        .requestMatchers("/professor/**").hasRole("PROFESSOR")  // Restringir para usuários com a role "PROFESSOR"
                        .requestMatchers("/home").authenticated()  // Garantir que a página home só pode ser acessada por usuários autenticados
                        .anyRequest().authenticated()  // Qualquer outra URL exige login
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Página de login
                        .loginProcessingUrl("/perform_login")  // URL de processamento do login
                        .defaultSuccessUrl("/home", true)  // Página de sucesso após o login
                        .failureHandler(authenticationFailureHandler())  // Utilizando o handler para erros de login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL de logout
                        .logoutSuccessUrl("/login?logout=true")  // Página após o logout
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(usuarioService)  // Configura o UserDetailsService
                .passwordEncoder(passwordEncoder()); // Configura o PasswordEncoder

        return authenticationManagerBuilder.build(); // Constrói o AuthenticationManager
    }

}
