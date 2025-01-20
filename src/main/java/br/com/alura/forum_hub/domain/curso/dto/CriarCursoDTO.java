package br.com.alura.forum_hub.domain.curso.dto;

import br.com.alura.forum_hub.domain.curso.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarCursoDTO(

        @NotBlank String nome,
        @NotNull Categoria categoria
) {
}
