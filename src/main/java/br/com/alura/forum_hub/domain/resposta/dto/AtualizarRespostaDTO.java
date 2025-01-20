package br.com.alura.forum_hub.domain.resposta.dto;

public record AtualizarRespostaDTO(
        String mensagem,
        Boolean solucao,
        Boolean deletada
) {
}
