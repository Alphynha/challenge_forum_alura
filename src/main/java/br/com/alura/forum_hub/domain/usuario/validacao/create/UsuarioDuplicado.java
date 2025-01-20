package br.com.alura.forum_hub.domain.usuario.validacao.create;

import br.com.alura.forum_hub.domain.usuario.dto.CriarUsuarioDTO;
import br.com.alura.forum_hub.domain.usuario.repositorio.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDuplicado implements CriarUsuarioValidacao{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validar(CriarUsuarioDTO dados) {
        var usuarioDuplicado = usuarioRepository.findByUsername(dados.username());
        if (usuarioDuplicado != null) {
            throw new ValidationException("Este usuário já existe");
        }

        var emailDuplicado = usuarioRepository.findByEmail(dados.email());
        if (emailDuplicado != null) {
            throw new ValidationException("Este email já existe");
        }
    }
}
