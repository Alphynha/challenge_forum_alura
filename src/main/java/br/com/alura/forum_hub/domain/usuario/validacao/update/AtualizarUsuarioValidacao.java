package br.com.alura.forum_hub.domain.usuario.validacao.update;

import br.com.alura.forum_hub.domain.usuario.dto.AtualizarUsuarioDTO;
import br.com.alura.forum_hub.domain.usuario.repositorio.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AtualizarUsuarioValidacao implements AtualizarUsuarioValidar{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validar(AtualizarUsuarioDTO dados) {
        if (dados.email() != null) {
            var emailDuplicado = usuarioRepository.findByEmail(dados.email());
            if (emailDuplicado != null) {
                throw new ValidationException("Esse email está em uso");
            }
        }
    }
}
