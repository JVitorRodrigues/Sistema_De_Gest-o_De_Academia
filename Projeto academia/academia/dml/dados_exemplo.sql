
--  DML - Inserções, Atualizações e Deleções de Exemplo



INSERT INTO planos (nome, valor, duracao_meses) VALUES
    ('Mensal Básico',     99.90,  1),
    ('Trimestral',       269.90,  3),
    ('Semestral',        479.90,  6),
    ('Anual Premium',    899.90, 12);


INSERT INTO instrutores (nome, cpf, especialidade) VALUES
    ('Carlos Silva',   '11122233344', 'Musculação'),
    ('Ana Oliveira',   '55566677788', 'Yoga e Alongamento'),
    ('Bruno Ferreira', '99900011122', 'Crossfit');


INSERT INTO alunos (nome, cpf, data_nascimento, telefone, email) VALUES
    ('Lucas Mendes',    '12345678901', '1995-03-15', '86991110001', 'lucas@email.com'),
    ('Fernanda Costa',  '23456789012', '1999-07-22', '86991110002', 'fernanda@email.com'),
    ('Rafael Souza',    '34567890123', '1988-11-05', '86991110003', 'rafael@email.com'),
    ('Juliana Lima',    '45678901234', '2001-01-30', '86991110004', 'juliana@email.com'),
    ('Pedro Alves',     '56789012345', '1993-09-18', '86991110005', 'pedro@email.com');


INSERT INTO matriculas (id_aluno, id_plano, id_instrutor, data_inicio) VALUES
    (1, 1, 1, '2025-01-10'),
    (2, 2, 2, '2025-02-01'),
    (3, 3, 1, '2025-01-15'),
    (4, 4, 3, '2025-03-01'),
    (5, 1, NULL, '2025-03-20');

-- ---- EXEMPLOS DE UPDATE ------------------------------------
-- Atualizar telefone de um aluno
UPDATE alunos SET telefone = '86999990001' WHERE id_aluno = 1;

-- Cancelar uma matrícula
UPDATE matriculas SET status = 'cancelada' WHERE id_matricula = 5;

-- Reajustar valor de um plano
UPDATE planos SET valor = 109.90 WHERE id_plano = 1;

-- ---- EXEMPLOS DE DELETE ------------------------------------
-- Deletar aluno (cascateia matrícula automaticamente)
-- DELETE FROM alunos WHERE id_aluno = 5;

-- Remover instrutor sem matrículas ativas
-- DELETE FROM instrutores WHERE id_instrutor = 3;
