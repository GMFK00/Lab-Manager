package com.labmanager.labmanager.service;

import com.labmanager.labmanager.domain.Usuario;
import com.labmanager.labmanager.domain.enums.Papel;
import com.labmanager.labmanager.repository.UsuarioRepository;
import lombok.Data;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Data
@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Injetando o PasswordEncoder no construtor
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar o usuário no banco de dados com base no email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return User.builder()
                .username(usuario.getEmail())  // Definindo o nome de usuário como o email
                .password(usuario.getSenha())  // A senha criptografada
                .roles(usuario.getPapel().name())  // Definindo os papéis do usuário (como "ROLE_ADMIN")
                .build();
    }

    // Função para criar um novo usuário com a senha criptografada
    public Usuario criarUsuario(Usuario usuario) {
        // Verifica se o email é válido e define o papel do usuário
        String email = usuario.getEmail();
        if (email.endsWith("@professor.uema.br")) {
            usuario.setPapel(Papel.PROFESSOR);
        } else if (usuario.getPapel() == null) {
            usuario.setPapel(Papel.USER); // Papel USER como padrão para email genérico
        }

        // Define o usuário como ativo por padrão
        usuario.setAtivo(true);

        // Criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        // Salva o usuário no banco de dados
        return usuarioRepository.save(usuario);
    }
}
