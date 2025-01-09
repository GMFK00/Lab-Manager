package com.labmanager.labmanager.controller;

import com.labmanager.labmanager.domain.Relatorio;
import com.labmanager.labmanager.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping
    public String listarRelatorios(Model model) {
        model.addAttribute("relatorios", relatorioService.listarRelatorios());
        return "relatorios";  // Lista de relatórios
    }

    @GetMapping("/adicionar")
    public String mostrarFormularioAdicionar(Model model) {
        model.addAttribute("relatorio", new Relatorio());
        return "formRelatorio";  // Formulário de adição de relatório
    }

    @PostMapping("/adicionar")
    public String adicionarRelatorio(@ModelAttribute Relatorio relatorio, RedirectAttributes redirectAttributes) {
        relatorioService.adicionarRelatorio(relatorio);
        redirectAttributes.addFlashAttribute("message", "Relatório gerado com sucesso!");
        return "redirect:/admin/relatorios";  // Redireciona após adicionar
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Relatorio relatorio = relatorioService.buscarRelatorioPorId(id);
        model.addAttribute("relatorio", relatorio);
        return "formRelatorio";  // Formulário de edição de relatório
    }

    @PostMapping("/editar/{id}")
    public String editarRelatorio(@PathVariable Long id, @ModelAttribute Relatorio relatorio, RedirectAttributes redirectAttributes) {
        relatorio.setId(id);
        relatorioService.editarRelatorio(relatorio);
        redirectAttributes.addFlashAttribute("message", "Relatório editado com sucesso!");
        return "redirect:/admin/relatorios";  // Redireciona após editar
    }

    @GetMapping("/excluir/{id}")
    public String excluirRelatorio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        relatorioService.excluirRelatorio(id);
        redirectAttributes.addFlashAttribute("message", "Relatório excluído com sucesso!");
        return "redirect:/admin/relatorios";  // Redireciona após excluir
    }
}
