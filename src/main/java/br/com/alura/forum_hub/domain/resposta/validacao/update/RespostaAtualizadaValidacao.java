package br.com.alura.forum_hub.domain.resposta.validacao.update;

import br.com.alura.forum_hub.domain.resposta.dto.AtualizarRespostaDTO;

public interface RespostaAtualizadaValidacao {

    void validar(AtualizarRespostaDTO dados,
                 Long respostaId);
}
