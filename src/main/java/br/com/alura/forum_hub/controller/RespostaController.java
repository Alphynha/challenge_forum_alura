package br.com.alura.forum_hub.controller;

import br.com.alura.forum_hub.domain.resposta.Resposta;
import br.com.alura.forum_hub.domain.resposta.dto.AtualizarRespostaDTO;
import br.com.alura.forum_hub.domain.resposta.dto.CriarRespostaDTO;
import br.com.alura.forum_hub.domain.resposta.dto.DetalhesRespostaDTO;
import br.com.alura.forum_hub.domain.resposta.repository.RespostaRepository;
import br.com.alura.forum_hub.domain.resposta.validacao.create.RespostaCriadaValidacao;
import br.com.alura.forum_hub.domain.resposta.validacao.update.RespostaAtualizadaValidacao;
import br.com.alura.forum_hub.domain.topico.Status;
import br.com.alura.forum_hub.domain.topico.Topico;
import br.com.alura.forum_hub.domain.topico.repository.TopicoRepository;
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
@RequestMapping("/respostas")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Resposta", description = "Uma resposta como sulução")
public class RespostaController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    List<RespostaCriadaValidacao> criadaValidacaoList;

    @Autowired
    List<RespostaAtualizadaValidacao> atualizadaValidacaosList;

    @PostMapping
    @Transactional
    @Operation(summary = "Registrar respostas")
    public ResponseEntity<DetalhesRespostaDTO> criarResposta(@RequestBody @Valid CriarRespostaDTO criarRespostaDTO,
                                                             UriComponentsBuilder uriBuilder){
        criadaValidacaoList.forEach(v -> v.validar(criarRespostaDTO));

        Usuario usuario = usuarioRepository.getReferenceById(criarRespostaDTO.usuarioId());
        Topico topico = topicoRepository.findById(criarRespostaDTO.topicoId()).get();

        var resposta = new Resposta(criarRespostaDTO, usuario, topico);
        respostaRepository.save(resposta);

        var uri = uriBuilder.path("/respostas/{id}").buildAndExpand(resposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalhesRespostaDTO(resposta));

    }

    @GetMapping("/topico/{topicoId}")
    @Operation(summary = "Ver respostas de um tema")
    public ResponseEntity<Page<DetalhesRespostaDTO>>
    verRespostasDeTopico(@PageableDefault(size = 5, sort = {"ultimaAtualizacao"},
            direction = Sort.Direction.ASC) Pageable pageable, @PathVariable Long topicoId){
        var pagina = respostaRepository.findAllByTopicoId(topicoId, pageable).map(DetalhesRespostaDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/usuario/{nomeUsuario}")
    @Operation(summary = "Ver todas as respostas de um usuário.")
    public ResponseEntity<Page<DetalhesRespostaDTO>>
    verRespostasDeUsuario(@PageableDefault(size = 5, sort = {"ultimaAtualizacao"},
            direction = Sort.Direction.ASC)Pageable pageable, @PathVariable Long usuarioId){
        var pagina = respostaRepository.findAllByUsuarioId(usuarioId, pageable).map(DetalhesRespostaDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver uma resposta")
    public ResponseEntity<DetalhesRespostaDTO> verResposta(@PathVariable Long id){
        Resposta resposta = respostaRepository.getReferenceById(id);

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
    @Operation(summary = "Atualizar Resposta.")
    public ResponseEntity<DetalhesRespostaDTO> atualizarResposta(@RequestBody @Valid AtualizarRespostaDTO atualizarRespostaDTO, @PathVariable Long id){
        atualizadaValidacaosList.forEach(v -> v.validar(atualizarRespostaDTO, id));
        Resposta resposta = respostaRepository.getReferenceById(id);
        resposta.atualizarResposta(atualizarRespostaDTO);

        if(atualizarRespostaDTO.solucao()){
            var temaResultado = topicoRepository.getReferenceById(resposta.getTopico().getId());
            temaResultado.setStatus(Status.Closed);
        }

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

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Deletar uma resposta")
    public ResponseEntity<?> deletarResposta(@PathVariable Long id){
        Resposta resposta = respostaRepository.getReferenceById(id);
        resposta.deletarResposta();
        return ResponseEntity.noContent().build();
    }
}
