package br.com.alura.forum_hub.domain.resposta.validacao.create;

import br.com.alura.forum_hub.domain.resposta.dto.CriarRespostaDTO;

public interface RespostaCriadaValidacao {
    void validar(CriarRespostaDTO dados);
}
