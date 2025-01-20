package br.com.alura.forum_hub.domain.resposta.validacao.create;

import br.com.alura.forum_hub.domain.resposta.dto.CriarRespostaDTO;
import br.com.alura.forum_hub.domain.topico.Status;
import br.com.alura.forum_hub.domain.topico.repository.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RespostaTopicoValidacao implements RespostaCriadaValidacao {

    @Autowired
    private TopicoRepository topicoRepository;

    @Override
    public void validar(CriarRespostaDTO dados) {
        var topicoExiste = topicoRepository.existsById(dados.topicoId());

        if (!topicoExiste) {
            throw new ValidationException(("Este tópico não existe."));
        }

        var topicoAberto = topicoRepository.findById(dados.topicoId()).get().getStatus();

        if (topicoAberto != Status.Open) {
            throw new ValidationException("Este tópico não está aberto.");
        }
    }
}
