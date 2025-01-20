CREATE TABLE cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ativo BIT(1) NOT NULL,
    categoria ENUM('Frontend', 'Backend', 'Fullstack', 'DevOps', 'IA') NOT NULL,
    nome VARCHAR(255) NOT NULL
);