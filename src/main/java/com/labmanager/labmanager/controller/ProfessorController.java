package com.labmanager.labmanager.controller;

import com.labmanager.labmanager.domain.Agendamento;
import com.labmanager.labmanager.domain.Usuario;
import com.labmanager.labmanager.service.AgendamentoService;
import com.labmanager.labmanager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private UsuarioService usuarioService;

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
        return "redirect:/professor/agendamentos";
    }

    @GetMapping("/agendamentos/editar/{id}")
    public String editarAgendamento(@PathVariable Long id, Model model, Principal principal) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(principal.getName());
        Agendamento agendamento = agendamentoService.buscarAgendamentoPorId(id);

        if (!agendamento.getUsuario().getId().equals(usuarioLogado.getId())) {
            return "redirect:/professor/agendamentos";
        }

        model.addAttribute("agendamento", agendamento);
        return "formAgendamento";
    }

    @PostMapping("/agendamentos/editar/{id}")
    public String atualizarAgendamento(@PathVariable Long id, @ModelAttribute Agendamento agendamento, Principal principal) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(principal.getName());

        // Verifica se o usuário no agendamento é nulo e define o usuário logado
        if (agendamento.getUsuario() == null || agendamento.getUsuario().getId() == null) {
            agendamento.setUsuario(usuarioLogado);
        }

        // Verifica se o usuário tem permissão para editar o agendamento
        if (!agendamento.getUsuario().getId().equals(usuarioLogado.getId())) {
            return "redirect:/professor/agendamentos";
        }

        agendamento.setId(id);
        agendamentoService.editarAgendamento(agendamento);
        return "redirect:/professor/agendamentos";
    }


    @GetMapping("/agendamentos/deletar/{id}")
    public String deletarAgendamento(@PathVariable Long id, Principal principal) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(principal.getName());
        Agendamento agendamento = agendamentoService.buscarAgendamentoPorId(id);

        if (agendamento.getUsuario().getId().equals(usuarioLogado.getId())) {
            agendamentoService.excluirAgendamento(id);
        }

        return "redirect:/professor/agendamentos";
    }

    @GetMapping("/agendamentos/horarios-disponiveis")
    @ResponseBody
    public List<String> horariosDisponiveis(@RequestParam String data, @RequestParam(required = false) Long agendamentoId) {
        LocalDate date = LocalDate.parse(data);
        return agendamentoService.buscarHorariosDisponiveis(date, agendamentoId);
    }

    @GetMapping("/usuarios/{id}")
    @ResponseBody
    public Usuario buscarUsuarioPorId(@PathVariable Long id) {
        return usuarioService.buscarUsuarioPorId(id);
    }

}
