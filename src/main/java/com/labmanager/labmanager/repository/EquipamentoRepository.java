package com.labmanager.labmanager.repository;

import com.labmanager.labmanager.domain.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    // Buscar equipamentos por status (ex.: Disponível, Em manutenção)
    List<Equipamento> findByStatus(String status);

    // Busca equipamentos por nome.
    List<Equipamento> findByNomeContaining(String nome);
}
