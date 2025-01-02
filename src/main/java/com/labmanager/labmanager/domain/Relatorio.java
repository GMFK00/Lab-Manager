package com.labmanager.labmanager.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data // Lombok gera automaticamente getters, setters, toString, equals e hashCode
@NoArgsConstructor // Lombok gera o construtor sem argumentos
@AllArgsConstructor // Lombok gera o construtor com todos os argumentos
@Entity
@Table(name = "relatorios")
public class Relatorio {

    // FAZER VALIDAÇÕES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataGeracao;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String conteudo;
}
