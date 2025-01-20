package br.com.alura.forum_hub.domain.usuario.validacao.create;

import br.com.alura.forum_hub.domain.usuario.dto.CriarUsuarioDTO;

public interface CriarUsuarioValidacao {
    void validar(CriarUsuarioDTO dados);
}
