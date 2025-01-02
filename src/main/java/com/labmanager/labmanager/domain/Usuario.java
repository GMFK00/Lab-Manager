package com.labmanager.labmanager.domain;

import com.labmanager.labmanager.domain.enums.Papel;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data // Lombok gera automaticamente getters, setters, toString, equals e hashCode
@NoArgsConstructor // Lombok gera o construtor sem argumentos
@AllArgsConstructor // Lombok gera o construtor com todos os argumentos
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome completo do usuário.
    @NotBlank(message = "O nome é obrigatório.") // Validação de campo não vazio e não contendo apenas espaços em branco
    @Column(nullable = false)
    private String nome;

    // Email usado para login.
    @NotBlank(message = "O email é obrigatório.") // Validação de campo não vazio e não contendo apenas espaços em branco
    @Email(message = "Por favor, insira um email válido.") // Validação de email
    @Column(nullable = false, unique = true) // único e obrigatório
    private String email;

    // Senha criptografada
    @NotBlank(message = "A senha não pode ser vazia.") // Validação para garantir que a senha não seja vazia ou nula
    @Column(nullable = false) // obrigatória
    private String senha;

    // Define o tipo de usuário (ADMIN, FUNCIONARIO, PROFESSOR, USER).
    @Enumerated(EnumType.STRING) // Salva o nome do enum como texto no banco
    @Column(nullable = false)
    private Papel papel;

    // Indica se o usuário está ativo no sistema.
    @Column(nullable = false) // Status ativo ou inativo obrigatório
    private Boolean ativo;

    // Método para retornar o papel como String com o prefixo "ROLE_"
    public String getRole() {
        return "ROLE_" + papel.name();  // "ROLE_" + o nome do papel, ex: "ROLE_ADMIN"
    }

}
