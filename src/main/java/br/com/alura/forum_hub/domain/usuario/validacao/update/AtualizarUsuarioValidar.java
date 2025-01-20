package br.com.alura.forum_hub.domain.usuario.validacao.update;

import br.com.alura.forum_hub.domain.usuario.dto.AtualizarUsuarioDTO;

public interface AtualizarUsuarioValidar {
    void validar(AtualizarUsuarioDTO dados);
}
