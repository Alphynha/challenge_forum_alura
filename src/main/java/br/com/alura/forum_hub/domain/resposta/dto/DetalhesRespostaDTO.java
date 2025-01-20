package br.com.alura.forum_hub.domain.resposta.dto;

import br.com.alura.forum_hub.domain.resposta.Resposta;

import java.time.LocalDateTime;

public record DetalhesRespostaDTO(
        Long id,
        String mensagem,
        LocalDateTime dataCriacao,
        LocalDateTime ultimaAtualizacao,
        Boolean solucao,
        Boolean deletada,
        Long usuarioId,
        String username,
        Long topicoId,
        String topico
) {

    public DetalhesRespostaDTO(Resposta resposta) {
        this (
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
    }
}
