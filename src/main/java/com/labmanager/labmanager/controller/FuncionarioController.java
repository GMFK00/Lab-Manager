package com.labmanager.labmanager.controller;

import com.labmanager.labmanager.domain.Agendamento;
import com.labmanager.labmanager.domain.Equipamento;
import com.labmanager.labmanager.domain.Usuario;
import com.labmanager.labmanager.service.AgendamentoService;
import com.labmanager.labmanager.service.EquipamentoService;
import com.labmanager.labmanager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private EquipamentoService equipamentoService;

    @Autowired
    private UsuarioService usuarioService;

    // ===============================
    // Gerenciamento de Agendamentos
    // ===============================

    @GetMapping("/agendamentos")
    public String listarAgendamentos(Model model, Principal principal) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(principal.getName());
        List<Agendamento> agendamentos = agendamentoService.listarAgendamentosPorUsuario(usuarioLogado.getId());
        model.addAttribute("agendamentos", agendamentos);
        return "agendamentos";
    }

    @GetMapping("/agendamentos/criar")
    public String criarAgendamento(Model model) {
        model.addAttribute("agendamento", new Agendamento());
        return "formAgendamento";
    }

    @PostMapping("/agendamentos/salvar")
    public String salvarAgendamento(@ModelAttribute Agendamento agendamento, Principal principal) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(principal.getName());
        agendamento.setUsuario(usuarioLogado);
        agendamentoService.salvarAgendamento(agendamento);
        return "redirect:/funcionario/agendamentos";
    }

    @GetMapping("/agendamentos/editar/{id}")
    public String editarAgendamento(@PathVariable Long id, Model model, Principal principal) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(principal.getName());
        Agendamento agendamento = agendamentoService.buscarAgendamentoPorId(id);

        if (agendamento.getUsuario() == null || !agendamento.getUsuario().getId().equals(usuarioLogado.getId())) {
            return "redirect:/funcionario/agendamentos";
        }

        List<String> horariosDisponiveis = agendamentoService.buscarHorariosDisponiveis(agendamento.getData(), id);
        model.addAttribute("agendamento", agendamento);
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);

        return "formAgendamento";
    }


    @PostMapping("/agendamentos/editar/{id}")
    public String atualizarAgendamento(@PathVariable Long id, @ModelAttribute Agendamento agendamento, Principal principal) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(principal.getName());

        if (agendamento.getUsuario() == null || agendamento.getUsuario().getId() == null) {
            agendamento.setUsuario(usuarioLogado);
        }

        if (!agendamento.getUsuario().getId().equals(usuarioLogado.getId())) {
            return "redirect:/funcionario/agendamentos";
        }

        agendamento.setId(id);
        agendamentoService.editarAgendamento(agendamento);
        return "redirect:/funcionario/agendamentos";
    }

    @GetMapping("/agendamentos/deletar/{id}")
    public String deletarAgendamento(@PathVariable Long id, Principal principal) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(principal.getName());
        Agendamento agendamento = agendamentoService.buscarAgendamentoPorId(id);

        if (agendamento.getUsuario().getId().equals(usuarioLogado.getId())) {
            agendamentoService.excluirAgendamento(id);
        }

        return "redirect:/funcionario/agendamentos";
    }

    // ===============================
    // Gerenciamento de Equipamentos
    // ===============================

    @GetMapping("/equipamentos")
    public String listarEquipamentos(Model model) {
        model.addAttribute("equipamentos", equipamentoService.listarEquipamentos());
        return "equipamentos";
    }

    @GetMapping("/equipamentos/editar/{id}")
    public String editarEquipamento(@PathVariable Long id, Model model) {
        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(id);
        model.addAttribute("equipamento", equipamento);
        return "formEquipamento";
    }

    @PostMapping("/equipamentos/editar/{id}")
    public String atualizarEquipamento(@PathVariable Long id, @ModelAttribute Equipamento equipamento) {
        Equipamento equipamentoExistente = equipamentoService.buscarEquipamentoPorId(id);

        // Mantém apenas os campos que podem ser editados
        equipamentoExistente.setDescricao(equipamento.getDescricao());
        equipamentoExistente.setStatus(equipamento.getStatus());
        equipamentoExistente.setObservacoes(equipamento.getObservacoes());

        equipamentoService.editarEquipamento(equipamentoExistente);
        return "redirect:/funcionario/equipamentos";
    }

    @GetMapping("/agendamentos/horarios-disponiveis")
    @ResponseBody
    public List<String> horariosDisponiveis(@RequestParam String data, @RequestParam(required = false) Long agendamentoId) {
        LocalDate date = LocalDate.parse(data);
        return agendamentoService.buscarHorariosDisponiveis(date, agendamentoId);
    }
}
