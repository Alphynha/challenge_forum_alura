package br.com.alura.forum_hub.domain.topico.validacao.create;

import br.com.alura.forum_hub.domain.curso.repository.CursoRepository;
import br.com.alura.forum_hub.domain.topico.dto.CriarTopicoDTO;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CursoCriadoValidacao implements TopicoCriadoValidacao {

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    public void validar(CriarTopicoDTO dados) {
        var ExisteCurso = cursoRepository.existsById(dados.cursoId());
        if (!ExisteCurso) {
            throw new ValidationException("Este curso não existe");
        }

        var cursoHabilitado = cursoRepository.findById(dados.cursoId()).get().getAtivo();
        if(!cursoHabilitado) {
            throw new ValidationException("Este curso não está disponível neste momento.");
        }
    }
}
