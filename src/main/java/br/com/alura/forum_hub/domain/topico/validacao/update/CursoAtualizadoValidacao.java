package br.com.alura.forum_hub.domain.topico.validacao.update;

import br.com.alura.forum_hub.domain.curso.repository.CursoRepository;
import br.com.alura.forum_hub.domain.topico.dto.AtualizarTopicoDTO;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CursoAtualizadoValidacao implements TopicoAtualizadoValidar {

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    public void validar(AtualizarTopicoDTO dados) {
        if (dados.cursoId() != null) {
            var ExisteCurso = cursoRepository.existsById(dados.cursoId());
            if (!ExisteCurso) {
                throw new ValidationException("Este curso não existe");
            }

            var cursoHabilitado = cursoRepository.findById(dados.cursoId()).get().getAtivo();
            if (!cursoHabilitado) {
                throw new ValidationException("Este curso não está disponível no momento.");
            }
        }
    }
}
