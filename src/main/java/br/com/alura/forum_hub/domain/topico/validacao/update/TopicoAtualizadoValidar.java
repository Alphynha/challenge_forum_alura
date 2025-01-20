package br.com.alura.forum_hub.domain.topico.validacao.update;

import br.com.alura.forum_hub.domain.topico.dto.AtualizarTopicoDTO;

public interface TopicoAtualizadoValidar {

    void validar(AtualizarTopicoDTO dados);
}
