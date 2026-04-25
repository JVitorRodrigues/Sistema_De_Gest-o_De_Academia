
--  DQL - Consultas do Sistema de Academia


-- 1. Listar todos os alunos ordenados por nome
SELECT id_aluno, nome, cpf, telefone, email, data_cadastro
FROM alunos
ORDER BY nome;

-- 2. Buscar aluno por nome (filtro - usado na tela de Alunos)
SELECT id_aluno, nome, cpf, telefone, email
FROM alunos
WHERE nome ILIKE '%lucas%'
ORDER BY nome;

-- 3. INNER JOIN: matrículas com aluno e plano (ambos obrigatórios)
SELECT m.id_matricula,
       a.nome        AS aluno,
       p.nome        AS plano,
       p.valor       AS valor_plano,
       m.status,
       m.data_inicio
FROM matriculas m
INNER JOIN alunos  a ON m.id_aluno = a.id_aluno
INNER JOIN planos  p ON m.id_plano = p.id_plano
ORDER BY m.id_matricula;

-- 4. INNER JOIN + LEFT JOIN: inclui instrutor (pode ser nulo)
--    Esta é a consulta principal da tela de Matrículas
SELECT m.id_matricula,
       a.nome             AS aluno,
       p.nome             AS plano,
       p.valor            AS valor_plano,
       i.nome             AS instrutor,
       i.especialidade,
       m.status,
       m.data_inicio
FROM matriculas m
INNER JOIN alunos      a ON m.id_aluno     = a.id_aluno
INNER JOIN planos      p ON m.id_plano     = p.id_plano
LEFT  JOIN instrutores i ON m.id_instrutor = i.id_instrutor
ORDER BY a.nome;

-- 5. Filtrar somente matrículas ativas
SELECT m.id_matricula, a.nome AS aluno, p.nome AS plano,
       i.nome AS instrutor, m.status, m.data_inicio
FROM matriculas m
INNER JOIN alunos      a ON m.id_aluno     = a.id_aluno
INNER JOIN planos      p ON m.id_plano     = p.id_plano
LEFT  JOIN instrutores i ON m.id_instrutor = i.id_instrutor
WHERE m.status = 'ativa'
ORDER BY m.data_inicio DESC;

-- 6. Alunos SEM matrícula (LEFT JOIN + IS NULL)
SELECT a.id_aluno, a.nome, a.email
FROM alunos a
LEFT JOIN matriculas m ON a.id_aluno = m.id_aluno
WHERE m.id_matricula IS NULL
ORDER BY a.nome;

-- 7. Planos ordenados por valor
SELECT id_plano, nome, valor, duracao_meses
FROM planos
ORDER BY valor ASC;

-- 8. Contagem de matrículas por plano
SELECT p.nome AS plano, COUNT(m.id_matricula) AS total_matriculas
FROM planos p
LEFT JOIN matriculas m ON p.id_plano = m.id_plano
GROUP BY p.id_plano, p.nome
ORDER BY total_matriculas DESC;
