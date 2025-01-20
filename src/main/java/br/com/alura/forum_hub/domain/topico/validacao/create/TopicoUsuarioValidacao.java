package br.com.alura.forum_hub.domain.topico.validacao.create;

import br.com.alura.forum_hub.domain.topico.dto.CriarTopicoDTO;
import br.com.alura.forum_hub.domain.usuario.repositorio.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicoUsuarioValidacao implements TopicoCriadoValidacao{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validar(CriarTopicoDTO dados) {
        var existeUsuario = usuarioRepository.existsById(dados.usuarioId());
        if (!existeUsuario) {
            throw new ValidationException("Este usuário não existe");
        }

        var usuarioHabilitado = usuarioRepository.findById(dados.usuarioId()).get().getEnabled();
        if (!usuarioHabilitado) {
            throw new ValidationException("Este usuário está off.");
        }
    }
}
