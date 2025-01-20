package br.com.alura.forum_hub.domain.usuario.dto;

import br.com.alura.forum_hub.domain.usuario.Role;
import br.com.alura.forum_hub.domain.usuario.Usuario;

public record DetalhesUsuarioDTO(
        Long id,
        String username,
        Role role,
        String nome,
        String sobrenome,
        String email,
        Boolean enabled
) {

    public DetalhesUsuarioDTO(Usuario usuario) {
        this(usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getEnabled());

    }
}
