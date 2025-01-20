package br.com.alura.forum_hub.domain.topico.validacao.create;

import br.com.alura.forum_hub.domain.topico.dto.CriarTopicoDTO;

public interface TopicoCriadoValidacao {

    void validar(CriarTopicoDTO dados);
}
