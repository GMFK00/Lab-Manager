package com.labmanager.labmanager.repository;

import com.labmanager.labmanager.domain.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    // Buscar disciplinas por professor
    List<Disciplina> findByProfessorId(Long professorId);

    // Buscar disciplinas por nome (filtro parcial)
    List<Disciplina> findByNomeContaining(String nome);

    // Buscar disciplina por código
    Optional<Disciplina> findByCodigo(String codigo);
}
