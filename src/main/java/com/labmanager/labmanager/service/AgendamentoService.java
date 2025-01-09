package com.labmanager.labmanager.service;

import com.labmanager.labmanager.domain.Agendamento;
import com.labmanager.labmanager.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public List<Agendamento> listarAgendamentos() {
        return agendamentoRepository.findAll();
    }

    public void salvarAgendamento(Agendamento agendamento) {
        if (agendamento.getData() == null) {
            throw new IllegalArgumentException("A data não pode ser nula.");
        }
        agendamentoRepository.save(agendamento);
    }

    public Agendamento buscarAgendamentoPorId(Long id) {
        return agendamentoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado."));
    }

    public void editarAgendamento(Agendamento agendamento) {
        StringBuilder errorMessage = new StringBuilder();

        if (agendamento.getData() == null) {
            errorMessage.append("A data não pode ser nula.\n");
        }
        if (agendamento.getHorario() == null) {
            errorMessage.append("O horário não pode ser nulo.\n");
        }
        if (agendamento.getId() != null && agendamento.getUsuario() == null) {
            errorMessage.append("O usuário não pode ser nulo ao editar um agendamento.\n");
        }

        if (errorMessage.length() > 0) {
            throw new IllegalArgumentException(errorMessage.toString());
        }

        agendamentoRepository.save(agendamento);
    }



    public void excluirAgendamento(Long id) {
        agendamentoRepository.deleteById(id);
    }

    public List<Agendamento> buscarPorData(LocalDate data, Long agendamentoId) {
        List<Agendamento> agendamentos = agendamentoRepository.findByData(data);
        return agendamentos.stream()
                .filter(agendamento -> !agendamento.getId().equals(agendamentoId))
                .collect(Collectors.toList());
    }

    public List<String> buscarHorariosDisponiveis(LocalDate data, Long agendamentoId) {
        List<Agendamento> agendamentos = buscarPorData(data, agendamentoId);
        List<String> horariosOcupados = agendamentos.stream()
                .map(Agendamento::getHorario)
                .collect(Collectors.toList());

        List<String> horariosPossiveis = List.of(
                "07:30 - 09:10", "09:10 - 10:50", "10:50 - 12:30",
                "13:30 - 15:10", "15:10 - 16:50", "16:50 - 18:30",
                "18:30 - 20:10", "20:10 - 21:50"
        );

        return horariosPossiveis.stream()
                .filter(horario -> !horariosOcupados.contains(horario))
                .collect(Collectors.toList());
    }


    public List<Agendamento> listarAgendamentosPorUsuario(Long usuarioId) {
        return agendamentoRepository.findByUsuarioId(usuarioId);
    }
}
