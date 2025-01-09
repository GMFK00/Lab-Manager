package com.labmanager.labmanager.repository;

import com.labmanager.labmanager.domain.Usuario;
import com.labmanager.labmanager.domain.enums.Papel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Verifica se já existe um usuário com o email fornecido
    boolean existsByEmail(String email);

    // Buscar usuário pelo email (usado no login)
    Optional<Usuario> findByEmail(String email);

    // Listar usuários por papel (ex.: ADMIN, FUNCIONARIO, PROFESSOR, USER)
    List<Usuario> findByPapel(Papel papel);

    // Opcional: Buscar usuário pelo email e status ativo
    Optional<Usuario> findByEmailAndAtivoTrue(String email);

    // Listar todos os usuários
    List<Usuario> findAll();

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    Optional<Usuario> findById(Long id);

}
