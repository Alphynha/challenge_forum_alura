package br.com.alura.forum_hub.controller;

import br.com.alura.forum_hub.domain.curso.Curso;
import br.com.alura.forum_hub.domain.curso.repository.CursoRepository;
import br.com.alura.forum_hub.domain.resposta.Resposta;
import br.com.alura.forum_hub.domain.resposta.dto.DetalhesRespostaDTO;
import br.com.alura.forum_hub.domain.resposta.repository.RespostaRepository;
import br.com.alura.forum_hub.domain.topico.Status;
import br.com.alura.forum_hub.domain.topico.Topico;
import br.com.alura.forum_hub.domain.topico.dto.AtualizarTopicoDTO;
import br.com.alura.forum_hub.domain.topico.dto.CriarTopicoDTO;
import br.com.alura.forum_hub.domain.topico.dto.DetalhesTopicoDTO;
import br.com.alura.forum_hub.domain.topico.repository.TopicoRepository;
import br.com.alura.forum_hub.domain.topico.validacao.create.TopicoCriadoValidacao;
import br.com.alura.forum_hub.domain.topico.validacao.update.TopicoAtualizadoValidar;
import br.com.alura.forum_hub.domain.usuario.Usuario;
import br.com.alura.forum_hub.domain.usuario.repositorio.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Topic", description = "Tópicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    RespostaRepository respostaRepository;

    @Autowired
    List<TopicoCriadoValidacao> criadoValidacaoList;

    @Autowired
    List<TopicoAtualizadoValidar> atualizadoValidars;

    @PostMapping
    @Transactional
    @Operation(summary = "Registrar novo tópico")
    public ResponseEntity<DetalhesTopicoDTO> criarTopico(@RequestBody @Valid CriarTopicoDTO criarTopicoDTO,
                                                         UriComponentsBuilder uriBuilder){
        criadoValidacaoList.forEach(v -> v.validar(criarTopicoDTO));

        Usuario usuario = usuarioRepository.findById(criarTopicoDTO.usuarioId()).get();
        Curso curso = cursoRepository.findById(criarTopicoDTO.cursoId()).get();
        Topico topico = new Topico(criarTopicoDTO, usuario, curso);

        topicoRepository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalhesTopicoDTO(topico));
    }

    @GetMapping("/all")
    @Operation(summary = "Busca todos os tópicos")
    public ResponseEntity<Page<DetalhesTopicoDTO>> mostrarTopicos(@PageableDefault(size = 5, sort = {"ultimaAtualizacao"}, direction = Sort.Direction.DESC) Pageable pageable){
        var pagina = topicoRepository.findAll(pageable).map(DetalhesTopicoDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Lista temas (abertos)")
    public ResponseEntity<Page<DetalhesTopicoDTO>> mostrarTopicosAbertos(@PageableDefault(size = 5, sort = {"ultimaAtualizacao"}, direction = Sort.Direction.DESC) Pageable pageable){
        var pagina = topicoRepository.findAllByStatusIsNot(Status.Deletado, pageable).map(DetalhesTopicoDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Tópico por ID")
    public ResponseEntity<DetalhesTopicoDTO> mostrarTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        var dadosTopico = new DetalhesTopicoDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getUltimaAtualizacao(),
                topico.getStatus(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getNome(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(dadosTopico);
    }

    @GetMapping("/{id}/solucion")
    @Operation(summary = "Ver respostas de um tópico")
    public ResponseEntity<DetalhesRespostaDTO> verRespostas(@PathVariable Long id){
        Resposta resposta = respostaRepository.getReferenceByTopicoId(id);

        var dadosResposta = new DetalhesRespostaDTO(
                resposta.getId(),
                resposta.getMensagem(),
                resposta.getDataCriacao(),
                resposta.getUltimaAtualizacao(),
                resposta.getSolucao(),
                resposta.getExcluida(),
                resposta.getUsuario().getId(),
                resposta.getUsuario().getUsername(),
                resposta.getTopico().getId(),
                resposta.getTopico().getTitulo()
        );
        return ResponseEntity.ok(dadosResposta);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Atualizar tópico")
    public ResponseEntity<DetalhesTopicoDTO> atualizarTopico(@RequestBody @Valid AtualizarTopicoDTO atualizarTopicoDTO, @PathVariable Long id){
        atualizadoValidars.forEach(v -> v.validar(atualizarTopicoDTO));

        Topico topico = topicoRepository.getReferenceById(id);

        if(atualizarTopicoDTO.cursoId() != null){
            Curso curso = cursoRepository.getReferenceById(atualizarTopicoDTO.cursoId());
            topico.atualizarTopicoComCurso(atualizarTopicoDTO, curso);
        }else{
            topico.atualizarTopico(atualizarTopicoDTO);
        }

        var dadosTopico = new DetalhesTopicoDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getUltimaAtualizacao(),
                topico.getStatus(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getNome(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(dadosTopico);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Deletar tópico")
    public ResponseEntity<?> deletarTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        topico.deletarTopico();
        return ResponseEntity.noContent().build();
    }
}
