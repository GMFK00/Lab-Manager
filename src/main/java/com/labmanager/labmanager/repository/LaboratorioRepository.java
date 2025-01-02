package com.labmanager.labmanager.repository;

import com.labmanager.labmanager.domain.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {

    // Buscar laboratório pelo nome
    Optional<LaboratorioRepository> findByNome(String nome);

    // Buscar laboratórios por capacidade mínima
    List<LaboratorioRepository> findByCapacidadeGreaterThanEqual(Integer capacidade);
}
