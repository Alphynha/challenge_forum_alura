package br.com.alura.forum_hub.domain.resposta.validacao.create;

import br.com.alura.forum_hub.domain.resposta.dto.CriarRespostaDTO;
import br.com.alura.forum_hub.domain.usuario.repositorio.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RespostaUsuarioValidacao implements RespostaCriadaValidacao {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validar(CriarRespostaDTO dados) {
        var usuarioExiste = usuarioRepository.existsById(dados.usuarioId());

        if (!usuarioExiste) {
            throw new ValidationException("Este usuário não existe");
        }

        var usuarioHabilitado = usuarioRepository.findById(dados.usuarioId()).get().isEnabled();

        if(!usuarioHabilitado) {
            throw new ValidationException("Este usuário não foi verificado.");
        }

    }
}
