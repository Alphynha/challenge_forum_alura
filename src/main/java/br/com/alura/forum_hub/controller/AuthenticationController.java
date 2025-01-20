package br.com.alura.forum_hub.controller;

import br.com.alura.forum_hub.domain.usuario.Usuario;
import br.com.alura.forum_hub.domain.usuario.dto.AuthenticarUsuarioDTO;
import br.com.alura.forum_hub.infra.seguranca.dto.JWTtokenDTO;
import br.com.alura.forum_hub.infra.service.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Authentication", description = "Login do usuário")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<JWTtokenDTO> autenticar(@RequestBody @Valid AuthenticarUsuarioDTO authenticarUsuarioDTO) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(authenticarUsuarioDTO.username(),
                authenticarUsuarioDTO.password());
        var usuarioLogado = authenticationManager.authenticate(authToken);
        var JWTtoken = tokenService.gerarToken((Usuario) usuarioLogado.getPrincipal());

        return ResponseEntity.ok(new JWTtokenDTO(JWTtoken));
    }
}
