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

import java.util.List;

@Data
@Service
public class UsuarioService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(usuario.getPapel().name())
                .build();
    }

    public Usuario criarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário com este email.");
        }
        String email = usuario.getEmail();
        if (email.endsWith("@professor.uema.br")) {
            usuario.setPapel(Papel.PROFESSOR);
        } else if (usuario.getPapel() == null) {
            usuario.setPapel(Papel.USER);
        }
        usuario.setAtivo(true);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }


    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public void excluirUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public void editarUsuario(Usuario usuario) {
        Usuario existingUsuario = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (!existingUsuario.getEmail().equals(usuario.getEmail()) && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Já existe outro usuário com este email.");
        }

        // Preserva o status ativo do usuário existente
        usuario.setAtivo(existingUsuario.getAtivo());

        // Reencode a senha, se necessário
        if (!usuario.getSenha().equals(existingUsuario.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        usuarioRepository.save(usuario);
    }

    public List<Usuario> buscarPorNomeParcial(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }


    public Usuario buscarPorEmail(String name) {
        return usuarioRepository.findByEmail(name)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
