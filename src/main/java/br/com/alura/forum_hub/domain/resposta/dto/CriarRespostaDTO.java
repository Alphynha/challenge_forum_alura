package br.com.alura.forum_hub.domain.resposta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarRespostaDTO(
        @NotBlank String mensagem,
        @NotNull Long usuarioId,
        @NotNull Long topicoId
) {
}
