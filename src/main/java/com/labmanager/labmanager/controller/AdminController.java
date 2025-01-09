package com.labmanager.labmanager.controller;

import com.labmanager.labmanager.domain.Agendamento;
import com.labmanager.labmanager.domain.Equipamento;
import com.labmanager.labmanager.service.AgendamentoService;
import com.labmanager.labmanager.service.EquipamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.labmanager.labmanager.domain.Usuario;
import com.labmanager.labmanager.service.UsuarioService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EquipamentoService equipamentoService;

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private UsuarioService usuarioService;

    // =======================================
    // Equipamentos (Admin)
    // =======================================

    @GetMapping("/equipamentos")
    public String equipamentos(Model model) {
        model.addAttribute("equipamentos", equipamentoService.listarEquipamentos());
        return "equipamentos";  // Aponta para a view de equipamentos
    }

    @GetMapping("/equipamentos/quantidadeDisponiveis")
    public String quantidadeEquipamentosDisponiveis(Model model) {
        model.addAttribute("equipamentosDisponiveis", equipamentoService.listarEquipamentosPorStatus("Disponível"));
        return "quantidadeDisponiveis";  // Visualização das quantidades disponíveis
    }

    @GetMapping("/equipamentos/quantidadeManutencao")
    public String quantidadeEquipamentosEmManutencao(Model model) {
        model.addAttribute("equipamentosManutencao", equipamentoService.listarEquipamentosPorStatus("Em manutenção"));
        return "quantidadeManutencao";  // Visualização de equipamentos em manutenção
    }

    @GetMapping("/equipamentos/registrar")
    public String registrarEquipamento(Model model) {
        model.addAttribute("equipamento", new Equipamento());  // Cria um novo equipamento para o formulário
        return "formEquipamento";  // Exibe o formulário de registro
    }

    @PostMapping("/equipamentos/registrar")
    public String adicionarEquipamento(@ModelAttribute Equipamento equipamento) {
        equipamentoService.adicionarEquipamento(equipamento);
        return "redirect:/admin/equipamentos";  // Redireciona para a lista de equipamentos após o registro
    }

    @GetMapping("/equipamentos/editar/{id}")
    public String editarEquipamento(@PathVariable Long id, Model model) {
        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(id);
        model.addAttribute("equipamento", equipamento);
        return "formEquipamento";  // Exibe o formulário de edição
    }

    @PostMapping("/equipamentos/editar/{id}")
    public String atualizarEquipamento(@PathVariable Long id, @ModelAttribute Equipamento equipamento) {
        // Garantir que a dataCadastro não seja modificada
        if (equipamento.getDataCadastro() == null) {
            Equipamento existingEquipamento = equipamentoService.buscarEquipamentoPorId(id);
            equipamento.setDataCadastro(existingEquipamento.getDataCadastro());
        }
        equipamento.setId(id);
        equipamentoService.editarEquipamento(equipamento);
        return "redirect:/admin/equipamentos";  // Redireciona após editar
    }

    @GetMapping("/equipamentos/excluir/{id}")
    public String excluirEquipamento(@PathVariable Long id) {
        equipamentoService.excluirEquipamento(id);
        return "redirect:/admin/equipamentos";  // Redireciona após a exclusão
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "usuarios"; // Aponta para a view de listagem de usuários
    }

    @GetMapping("/usuarios/{id}")
    @ResponseBody
    public Usuario buscarUsuarioPorId(@PathVariable Long id) {
        return usuarioService.buscarUsuarioPorId(id);
    }


    @GetMapping("/usuarios/criar")
    public String criarUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "formUsuario";
    }

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(@ModelAttribute Usuario usuario, Model model) {
        try {
            usuarioService.criarUsuario(usuario);
            return "redirect:/admin/usuarios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "formUsuario";
        }
    }

    @GetMapping("/usuarios/busca")
    @ResponseBody
    public List<Usuario> buscarUsuarios(@RequestParam("nome") String nome) {
        return usuarioService.buscarPorNomeParcial(nome);
    }


    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        return "formUsuario";
    }

    @PostMapping("/usuarios/editar/{id}")
    public String atualizarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario, Model model) {
        try {
            usuario.setId(id);
            usuarioService.editarUsuario(usuario);
            return "redirect:/admin/usuarios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "formUsuario";
        }
    }


    @GetMapping("/usuarios/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
        return "redirect:/admin/usuarios"; // Redireciona após a exclusão
    }

    @GetMapping("/agendamentos")
    public String listarAgendamentos(Model model) {
        model.addAttribute("agendamentos", agendamentoService.listarAgendamentos());
        return "agendamentos";
    }

    @GetMapping("/agendamentos/criar")
    public String criarAgendamento(Model model) {
        model.addAttribute("agendamento", new Agendamento());
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "formAgendamento";
    }

    @PostMapping("/agendamentos/salvar")
    public String salvarAgendamento(@ModelAttribute Agendamento agendamento, @RequestParam Long usuarioId, Model model) {
        try {
            if (usuarioId != null) {
                Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);
                agendamento.setUsuario(usuario);
            } else {
                throw new IllegalArgumentException("O usuário é obrigatório.");
            }
            if (agendamento.getData() == null) {
                throw new IllegalArgumentException("A data é obrigatória.");
            }
            agendamentoService.salvarAgendamento(agendamento);
            return "redirect:/admin/agendamentos";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao salvar agendamento: " + e.getMessage());
            model.addAttribute("agendamento", agendamento);
            model.addAttribute("usuarios", usuarioService.listarUsuarios());
            return "formAgendamento";
        }
    }

    @GetMapping("/agendamentos/editar/{id}")
    public String editarAgendamento(@PathVariable Long id, Model model) {
        Agendamento agendamento = agendamentoService.buscarAgendamentoPorId(id);
        List<String> horariosDisponiveis = agendamentoService.buscarHorariosDisponiveis(agendamento.getData(), id);
        model.addAttribute("agendamento", agendamento);
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "formAgendamento";
    }

    @PostMapping("/agendamentos/editar/{id}")
    public String atualizarAgendamento(@PathVariable Long id, @ModelAttribute Agendamento agendamento, @RequestParam(required = false) Long usuarioId) {
        try {
            // Verifica se o usuarioId foi enviado no request
            if (usuarioId != null) {
                Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);
                agendamento.setUsuario(usuario);
            } else if (agendamento.getUsuario() != null && agendamento.getUsuario().getId() != null) {
                Usuario usuario = usuarioService.buscarUsuarioPorId(agendamento.getUsuario().getId());
                agendamento.setUsuario(usuario);
            } else {
                throw new IllegalArgumentException("O usuário do agendamento não pode ser nulo.");
            }

            // Verifica se a data do agendamento está presente
            if (agendamento.getData() == null) {
                throw new IllegalArgumentException("A data do agendamento não pode ser nula.");
            }

            // Define o ID do agendamento e salva as alterações
            agendamento.setId(id);
            agendamentoService.editarAgendamento(agendamento);
            return "redirect:/admin/agendamentos";

        } catch (Exception e) {
            // Retorna para o formulário de edição em caso de erro
            return "formAgendamento";
        }
    }



    @GetMapping("/agendamentos/horarios-disponiveis")
    @ResponseBody
    public List<String> horariosDisponiveis(@RequestParam String data, @RequestParam(required = false) Long agendamentoId) {
        LocalDate date = LocalDate.parse(data);
        List<Agendamento> agendamentos = agendamentoService.buscarPorData(date, agendamentoId);
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


    @GetMapping("/agendamentos/deletar/{id}")
    public String deletarAgendamento(@PathVariable Long id) {
        agendamentoService.excluirAgendamento(id);
        return "redirect:/admin/agendamentos";
    }
}
