package com.labmanager.labmanager.controller;

import com.labmanager.labmanager.domain.Usuario;
import com.labmanager.labmanager.service.UsuarioService;
import com.labmanager.labmanager.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.constraints.Email;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository; // Injetando o repositório para verificar se o email já existe

    // Construtor que injeta o UsuarioService e o UsuarioRepository
    public AuthController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    // Exibe a página de login
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Exibe a página de registro
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("usuario", new Usuario()); // Adiciona um novo objeto Usuario ao modelo
        return "register";
    }

    // Método auxiliar para validar formato de email
    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Processa o registro do usuário
    @PostMapping("/register")
    public String registerUser(@Validated @ModelAttribute("usuario") Usuario usuario,
                               BindingResult bindingResult,
                               Model model) {
        // Verifica se o campo de nome está vazio
        if (usuario.getNome().isBlank()) {
            bindingResult.rejectValue("nome", "error.usuario", "O nome é obrigatório.");
        }

        // Verifica se o campo de email já existe no banco de dados
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            bindingResult.rejectValue("email", "error.usuario", "Este email já está registrado.");
        }

        // Validação do email: verifica se o email é válido
        if (!isEmailValid(usuario.getEmail())) {
            bindingResult.rejectValue("email", "error.usuario", "O email informado é inválido.");
        }

        // Se houver erros de validação (como nome vazio, email já registrado, email inválido ou campo obrigatório vazio), retorna à página de registro
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            usuarioService.criarUsuario(usuario); // Cria o usuário no banco de dados
        } catch (IllegalArgumentException e) {
            // Se ocorrer erro durante a criação (como senha vazia, por exemplo), adiciona erro de validação
            bindingResult.rejectValue("senha", "error.usuario", e.getMessage());
            return "register"; // Retorna para a página de registro com a mensagem de erro
        }

        // Redireciona para a página de sucesso após o registro
        return "redirect:/register-success";
    }

    // NÂO FUNCIONAL AINDA
    // Exibe a página de sucesso após o registro
    @GetMapping("/register-success")
    public String showSuccessPage() {
        return "register-success"; // Exibe a página de sucesso após o registro
    }
}
