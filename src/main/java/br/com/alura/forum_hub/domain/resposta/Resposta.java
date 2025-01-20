package br.com.alura.forum_hub.domain.resposta;

import br.com.alura.forum_hub.domain.resposta.dto.AtualizarRespostaDTO;
import br.com.alura.forum_hub.domain.resposta.dto.CriarRespostaDTO;
import br.com.alura.forum_hub.domain.topico.Topico;
import br.com.alura.forum_hub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "respostas")
@Entity(name = "Resposta")
@EqualsAndHashCode(of ="id")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;

    private Boolean solucao;
    private Boolean excluida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    public Resposta(CriarRespostaDTO criarRespostaDTO, Usuario usuario, Topico topico) {
        this.mensagem = criarRespostaDTO.mensagem();
        this.dataCriacao = LocalDateTime.now();
        this.ultimaAtualizacao = LocalDateTime.now();
        this.solucao = false;
        this.excluida = false;
        this.usuario = usuario;
        this.topico = topico;
    }

    public void atualizarResposta(AtualizarRespostaDTO atualizarRespostaDTO) {
        if (atualizarRespostaDTO.mensagem() != null) {
            this.mensagem = atualizarRespostaDTO.mensagem();
        }

        if (atualizarRespostaDTO.solucao() != null) {
            this.solucao = atualizarRespostaDTO.solucao();
        }
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    public void deletarResposta() {
        this.excluida = true;
    }
}
