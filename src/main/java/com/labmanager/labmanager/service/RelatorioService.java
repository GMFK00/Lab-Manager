package com.labmanager.labmanager.service;

import com.labmanager.labmanager.domain.Relatorio;
import com.labmanager.labmanager.repository.RelatorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private RelatorioRepository relatorioRepository;

    public List<Relatorio> listarRelatorios() {
        return relatorioRepository.findAll();
    }

    public Relatorio buscarRelatorioPorId(Long id) {
        return relatorioRepository.findById(id).orElse(null);
    }

    public void adicionarRelatorio(Relatorio relatorio) {
        relatorioRepository.save(relatorio);
    }

    public void editarRelatorio(Relatorio relatorio) {
        relatorioRepository.save(relatorio);
    }

    public void excluirRelatorio(Long id) {
        relatorioRepository.deleteById(id);
    }

    public void gerarRelatorio(Relatorio relatorio) {

    }
}
