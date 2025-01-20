package br.com.alura.forum_hub.domain.curso;

import br.com.alura.forum_hub.domain.curso.dto.AtualizarCursoDTO;
import br.com.alura.forum_hub.domain.curso.dto.CriarCursoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cursos")
@Entity(name = "Curso")
@EqualsAndHashCode(of ="id")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    private Boolean ativo;

    public Curso(CriarCursoDTO criarCursoDTO) {
        this.nome = criarCursoDTO.nome();
        this.categoria = criarCursoDTO.categoria();
        this.ativo = true;
    }
    public void atualizarCurso(AtualizarCursoDTO atualizarCursoDTO) {
        if (atualizarCursoDTO.nome() != null) {
            this.nome = atualizarCursoDTO.nome();
        }
        if (atualizarCursoDTO.categoria() != null) {
            this.categoria = atualizarCursoDTO.categoria();
        }
        if (atualizarCursoDTO.ativo() != null) {
            this.ativo = atualizarCursoDTO.ativo();
        }
    }

    public void deletarCurso() {
        this.ativo = false;
    }
}
