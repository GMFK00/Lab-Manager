package com.labmanager.labmanager.repository;

import com.labmanager.labmanager.domain.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Buscar agendamentos para um laboratório específico
    List<Agendamento> findByLaboratorioId(Long laboratorioId);

    // Buscar agendamentos por intervalo de datas
    List<Agendamento> findByDataHoraInicioBetween(LocalDateTime inicio, LocalDateTime fim);

    // Buscar agendamentos por tipo (ex.: Atividade, Disciplina)
    List<Agendamento> findByTipo(String tipo);

}
