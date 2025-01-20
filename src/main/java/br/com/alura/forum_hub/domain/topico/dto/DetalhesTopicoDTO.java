package br.com.alura.forum_hub.domain.topico.dto;

import br.com.alura.forum_hub.domain.curso.Categoria;
import br.com.alura.forum_hub.domain.topico.Status;
import br.com.alura.forum_hub.domain.topico.Topico;

import java.time.LocalDateTime;

public record DetalhesTopicoDTO(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        LocalDateTime ultimaAtualizacao,
        Status status,
        String usuario,
        String curso,
        Categoria categoriaCurso
) {
    public DetalhesTopicoDTO(Topico topico) {
        this(topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getUltimaAtualizacao(),
                topico.getStatus(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getNome(),
                topico.getCurso().getCategoria());
    }
}
