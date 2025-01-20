package br.com.alura.forum_hub.domain.curso.dto;

import br.com.alura.forum_hub.domain.curso.Categoria;

public record AtualizarCursoDTO(
        String nome,
        Categoria categoria,
        Boolean ativo
) {
}
