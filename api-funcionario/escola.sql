CREATE DATABASE IF NOT EXISTS escola;
USE escola;

-- Tabela original da Aula 15: funcionario
CREATE TABLE IF NOT EXISTS funcionario (
    id_funcionario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL,
    senha VARCHAR(60) NOT NULL,
    cargo VARCHAR(60) NOT NULL
);

INSERT INTO funcionario (nome, email, senha, cargo) VALUES
('Ana Silva', 'ana@teste.com', '123', 'Atendente'),
('Bruno Souza', 'bruno@teste.com', '123', 'Gerente'),
('Carla Nunes', 'carla@teste.com', 'abc', 'Caixa');

-- Nova tabela criada na database escola: aluno
CREATE TABLE IF NOT EXISTS aluno (
    id_aluno INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL,
    matricula VARCHAR(30) NOT NULL,
    curso VARCHAR(60) NOT NULL
);

INSERT INTO aluno (nome, email, matricula, curso) VALUES
('Lucas Pereira', 'lucas@escola.com', '2026001', 'Desenvolvimento de Sistemas'),
('Mariana Costa', 'mariana@escola.com', '2026002', 'Redes de Computadores'),
('Rafael Oliveira', 'rafael@escola.com', '2026003', 'Ciência da Computação');
