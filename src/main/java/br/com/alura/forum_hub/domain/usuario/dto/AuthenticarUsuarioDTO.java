package br.com.alura.forum_hub.domain.usuario.dto;

public record AuthenticarUsuarioDTO(
        String username,
        String password
) {
}
