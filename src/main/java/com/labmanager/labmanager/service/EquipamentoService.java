package com.labmanager.labmanager.service;

import com.labmanager.labmanager.domain.Equipamento;
import com.labmanager.labmanager.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    public List<Equipamento> listarEquipamentos() {
        return equipamentoRepository.findAll();
    }

    public void adicionarEquipamento(Equipamento equipamento) {
        equipamentoRepository.save(equipamento);
    }

    public Equipamento buscarEquipamentoPorId(Long id) {
        Optional<Equipamento> equipamento = equipamentoRepository.findById(id);
        return equipamento.orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado"));
    }

    public void editarEquipamento(Equipamento equipamento) {
        equipamentoRepository.save(equipamento);
    }

    public void excluirEquipamento(Long id) {
        equipamentoRepository.deleteById(id);
    }

    public Object listarEquipamentosPorStatus(String status) {
        return equipamentoRepository.findByStatus(status);
    }
}
