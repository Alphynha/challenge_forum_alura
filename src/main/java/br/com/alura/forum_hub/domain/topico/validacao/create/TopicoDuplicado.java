package br.com.alura.forum_hub.domain.topico.validacao.create;

import br.com.alura.forum_hub.domain.topico.dto.CriarTopicoDTO;
import br.com.alura.forum_hub.domain.topico.repository.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicoDuplicado implements TopicoCriadoValidacao{

    @Autowired
    private TopicoRepository topicoRepository;

    @Override
    public void validar(CriarTopicoDTO dados) {
        var topicoDuplicado = topicoRepository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem());
        if (topicoDuplicado) {
            throw new ValidationException("Este tópico já existe!. Veja /topicos/" + topicoRepository.findByTitulo(dados.titulo()).getId());
        }
    }
}
