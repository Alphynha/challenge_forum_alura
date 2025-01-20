package br.com.alura.forum_hub.domain.topico.dto;

import br.com.alura.forum_hub.domain.topico.Status;

public record AtualizarTopicoDTO(
        String titulo,
        String mensagem,
        Status status,
        Long cursoId
) {
}
