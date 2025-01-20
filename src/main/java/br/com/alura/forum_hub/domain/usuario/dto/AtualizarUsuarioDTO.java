package br.com.alura.forum_hub.domain.usuario.dto;

import br.com.alura.forum_hub.domain.usuario.Role;

public record AtualizarUsuarioDTO(
        String password,
        Role role,
        String nome,
        String sobrenome,
        String email,
        Boolean enabled
) {
}
