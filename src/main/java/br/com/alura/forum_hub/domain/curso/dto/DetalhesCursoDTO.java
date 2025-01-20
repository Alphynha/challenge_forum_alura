package br.com.alura.forum_hub.domain.curso.dto;

import br.com.alura.forum_hub.domain.curso.Categoria;
import br.com.alura.forum_hub.domain.curso.Curso;

public record DetalhesCursoDTO(

        Long id,
        String nome,
        Categoria categoria,
        Boolean ativo

) {

    public DetalhesCursoDTO(Curso curso) {
        this (
                curso.getId(),
                curso.getNome(),
                curso.getCategoria(),
                curso.getAtivo()
        );
    }

}
