CREATE TABLE respostas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    deletado BIT(1) NOT NULL,
    data_criacao DATETIME NOT NULL,
    mensagem VARCHAR(255) NOT NULL,
    solucao BIT(1) NOT NULL,
    ultima_atualizacao DATETIME NOT NULL,
    topico_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (topico_id) REFERENCES topicos(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);