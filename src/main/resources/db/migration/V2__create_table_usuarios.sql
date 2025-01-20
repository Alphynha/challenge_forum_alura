CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sobrenome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    enabled BIT(1) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'User', 'Espectador') NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE
);