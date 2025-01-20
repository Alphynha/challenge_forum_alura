package br.com.alura.forum_hub.controller;

import br.com.alura.forum_hub.domain.curso.Curso;
import br.com.alura.forum_hub.domain.curso.dto.AtualizarCursoDTO;
import br.com.alura.forum_hub.domain.curso.dto.CriarCursoDTO;
import br.com.alura.forum_hub.domain.curso.dto.DetalhesCursoDTO;
import br.com.alura.forum_hub.domain.curso.repository.CursoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cursos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Curso", description = "O curso pode estar presente em mais de uma categoria")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    @Operation(summary = "Registrar um novo curso")
    public ResponseEntity<DetalhesCursoDTO> criarTopico(@RequestBody @Valid CriarCursoDTO criarCursoDTO,
                                                        UriComponentsBuilder uriBuilder) {
        Curso curso = new Curso(criarCursoDTO);
        cursoRepository.save(curso);
        var uri = uriBuilder.path("/cursos{i}").buildAndExpand(curso.getId()).toUri();

        return ResponseEntity.created(uri).body(new DetalhesCursoDTO(curso));
    }

    @GetMapping("/all")
    @Operation(summary = "Todos os cursos")
    public ResponseEntity<Page<DetalhesCursoDTO>> listarCurso (@PageableDefault(size = 5, sort = {"id"})Pageable pageable) {
        var pagina = cursoRepository.findAll(pageable).map(DetalhesCursoDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Cursos ativos")
    public ResponseEntity<Page<DetalhesCursoDTO>> listarCursosAtivos (@PageableDefault(size = 5, sort = {"id"})Pageable pageable) {
        var pagina = cursoRepository.findAllByAtivoTrue(pageable).map(DetalhesCursoDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar curso por ID")
    public ResponseEntity<DetalhesCursoDTO> listarCursoPorId(@PathVariable Long id) {
        Curso curso = cursoRepository.getReferenceById(id);
        var detalhesDoCurso = new DetalhesCursoDTO(
                curso.getId(),
                curso.getNome(),
                curso.getCategoria(),
                curso.getAtivo()
        );
        return ResponseEntity.ok(detalhesDoCurso);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Atualizar dados do curso")
    public ResponseEntity<DetalhesCursoDTO> atualizarCurso(@RequestBody @Valid AtualizarCursoDTO atualizarCursoDTO,
                                                           @PathVariable Long id) {
        Curso curso = cursoRepository.getReferenceById(id);
        curso.atualizarCurso(atualizarCursoDTO);

        var detalhesDoCurso = new DetalhesCursoDTO(
                curso.getId(),
                curso.getNome(),
                curso.getCategoria(),
                curso.getAtivo()
        );
        return ResponseEntity.ok(detalhesDoCurso);
    }

    @DeleteMapping("{id}")
    @Transactional
    @Operation(summary = "Deletar curso")
    public ResponseEntity<?> deletarCurso(@PathVariable Long id) {
        Curso curso = cursoRepository.getReferenceById(id);
        curso.deletarCurso();
        return ResponseEntity.noContent().build();
    }
}
