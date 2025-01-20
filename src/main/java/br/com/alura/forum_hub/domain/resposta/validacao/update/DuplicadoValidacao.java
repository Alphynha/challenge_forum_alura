package br.com.alura.forum_hub.domain.resposta.validacao.update;

import br.com.alura.forum_hub.domain.resposta.Resposta;
import br.com.alura.forum_hub.domain.resposta.dto.AtualizarRespostaDTO;
import br.com.alura.forum_hub.domain.resposta.repository.RespostaRepository;
import br.com.alura.forum_hub.domain.topico.Status;
import br.com.alura.forum_hub.domain.topico.repository.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DuplicadoValidacao implements RespostaAtualizadaValidacao{

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Override
    public void validar(AtualizarRespostaDTO dados,
                        Long respostaId) {
        if (dados.solucao()) {
            Resposta resposta = respostaRepository.getReferenceById(respostaId);
            var topicoResultado = topicoRepository.getReferenceById(resposta.getTopico().getId());
            if (topicoResultado.getStatus() == Status.Closed) {
                throw new ValidationException("Este topico já está solucionado.");
            }
        }
    }
}
