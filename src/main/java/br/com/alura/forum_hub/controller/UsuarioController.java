package br.com.alura.forum_hub.controller;

import br.com.alura.forum_hub.domain.usuario.Usuario;
import br.com.alura.forum_hub.domain.usuario.dto.AtualizarUsuarioDTO;
import br.com.alura.forum_hub.domain.usuario.dto.CriarUsuarioDTO;
import br.com.alura.forum_hub.domain.usuario.dto.DetalhesUsuarioDTO;
import br.com.alura.forum_hub.domain.usuario.repositorio.UsuarioRepository;
import br.com.alura.forum_hub.domain.usuario.validacao.create.CriarUsuarioValidacao;
import br.com.alura.forum_hub.domain.usuario.validacao.update.AtualizarUsuarioValidacao;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuario", description = "Tópicos e Respostas")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    List<CriarUsuarioValidacao> criarUsuarioValidacoes;

    @Autowired
    List<AtualizarUsuarioValidacao> atualizarUsuarioValidacoes;

    @PostMapping
    @Transactional
    @Operation(summary = "Registrar usuário")
    public ResponseEntity<DetalhesUsuarioDTO> criarUsuario(@RequestBody @Valid CriarUsuarioDTO criarUsuarioDTO,
                                                           UriComponentsBuilder uriBuilder){
        criarUsuarioValidacoes.forEach(v -> v.validar(criarUsuarioDTO));

        String hashedPassword = passwordEncoder.encode(criarUsuarioDTO.password());
        Usuario usuario = new Usuario(criarUsuarioDTO, hashedPassword);

        usuarioRepository.save(usuario);
        var uri = uriBuilder.path("/usuarios/{username}").buildAndExpand(usuario.getUsername()).toUri();
        return ResponseEntity.created(uri).body(new DetalhesUsuarioDTO(usuario));
    }

    @GetMapping("/all")
    @Operation(summary = "Mostrar todos os usuários")
    public ResponseEntity<Page<DetalhesUsuarioDTO>> buscarUsuarios(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable){
        var pagina = usuarioRepository.findAll(pageable).map(DetalhesUsuarioDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Mostrat somentes usuários ativos")
    public ResponseEntity<Page<DetalhesUsuarioDTO>> buscarUsuariosAtivos(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable){
        var pagina = usuarioRepository.findAllByEnabledTrue(pageable).map(DetalhesUsuarioDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Buscar somente um usuário")
    public ResponseEntity<DetalhesUsuarioDTO> buscarUsuario(@PathVariable String username){
        Usuario usuario = (Usuario) usuarioRepository.findByUsername(username);
        var dadosUsuario = new DetalhesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(dadosUsuario);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "BuscarUsuarioID")
    public ResponseEntity<DetalhesUsuarioDTO>buscarUsuario(@PathVariable Long id){
        Usuario usuario = usuarioRepository.getReferenceById(id);
        var datosUsuario = new DetalhesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @PutMapping("/{username}")
    @Transactional
    @Operation(summary = "Atualizar Usuário")
    public ResponseEntity<DetalhesUsuarioDTO> atualizarUsuario(@RequestBody @Valid AtualizarUsuarioDTO atualizarUsuarioDTO, @PathVariable String username){
        atualizarUsuarioValidacoes.forEach(validar -> validar.validar(atualizarUsuarioDTO));

        Usuario usuario = (Usuario) usuarioRepository.findByUsername(username);

        if (atualizarUsuarioDTO.password() != null){
            String hashedPassword = passwordEncoder.encode(atualizarUsuarioDTO.password());
            usuario.atualizarUsuarioComSenha(atualizarUsuarioDTO, hashedPassword);

        }else {
            usuario.atualizarUsuario(atualizarUsuarioDTO);
        }

        var dadosUsuario = new DetalhesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(dadosUsuario);
    }

    @DeleteMapping("/{username}")
    @Transactional
    @Operation(summary = "Desabilitar usuário")
    public ResponseEntity<?> deletarUsuario(@PathVariable String username){
        Usuario usuario = (Usuario) usuarioRepository.findByUsername(username);
        usuario.deletarUsuario();
        return ResponseEntity.noContent().build();
    }
}