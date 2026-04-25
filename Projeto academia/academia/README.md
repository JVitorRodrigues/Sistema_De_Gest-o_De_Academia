## — Sistema de Gestão de Academia

Sistema de gerenciamento de academia desenvolvido em **Java (Swing)** com banco de dados **PostgreSQL**, como projeto avaliativo da disciplina de Banco de Dados.

---

## 📋 Descrição

O **Sistema de Academia** permite gerenciar os principais cadastros de uma academia: alunos, instrutores, planos e matrículas. A aplicação conta com tela de login, interface gráfica completa e operações CRUD em todas as entidades, além de consultas com `INNER JOIN` e `LEFT JOIN`.

---

## 🗂 Estrutura do Repositório

```
📁 academia-gym/
├── 📁 diagrama/      → Diagrama Entidade-Relacionamento (DER)
├── 📁 ddl/           → Scripts de criação das tabelas
├── 📁 dml/           → Scripts de inserção, atualização e deleção
├── 📁 dql/           → Scripts de consultas (filtros, JOINs, ordenação)
├── 📁 src/           → Código-fonte Java
└── README.md
```

---

## Tecnologias Utilizadas

| Camada       | Tecnologia                  |
|--------------|-----------------------------|
| Linguagem    | Java 17+                    |
| Interface    | Java Swing                  |
| Banco de dados | PostgreSQL 15+            |
| Driver JDBC  | postgresql-42.x.x.jar       |

---

## Modelo de Dados

O sistema possui **5 tabelas** com chaves primárias e estrangeiras:

- **usuarios** — controle de acesso ao sistema
- **alunos** — cadastro dos alunos da academia
- **instrutores** — cadastro dos instrutores
- **planos** — planos disponíveis (mensal, trimestral, etc.)
- **matriculas** — vínculo entre aluno, plano e instrutor (FK para as 3 tabelas)

---

## Como Configurar e Executar

### Pré-requisitos

- Java JDK 17 ou superior
- PostgreSQL instalado e rodando
- Driver JDBC do PostgreSQL ([download](https://jdbc.postgresql.org/download/))

### 1. Criar o banco de dados

```sql
CREATE DATABASE academia;
```

### 2. Executar os scripts SQL (nesta ordem)

```bash
psql -U postgres -d academia -f ddl/criar_tabelas.sql
psql -U postgres -d academia -f dml/dados_exemplo.sql
```

### 3. Configurar a conexão

Edite o arquivo `src/ConexaoBD.java` com suas credenciais:

```java
private static final String URL     = "jdbc:postgresql://localhost:5432/academia";
private static final String USUARIO = "postgres";
private static final String SENHA   = "1234";
```

### 4. Compilar

Coloque o arquivo `.jar` do driver JDBC na pasta `src/` e compile:

```bash
cd src
javac -cp ".;postgresql-42.x.x.jar" *.java          

```

### 5. Executar

```bash
java -cp ".;postgresql-42.x.x.jar" Login             

```

### Credenciais padrão de login

| Usuário | Senha    |
|---------|----------|
| admin   | admin123 |

---

## Screenshots da Aplicação

### Tela de Login
> ![alt text](<Captura de tela 2026-04-24 203855.png>)

### Menu Principal
> *(inserir print aqui)*

### Resultado de consulta com JOIN (Matrículas)
> *(inserir print aqui — botão "📊 Relatório JOIN" na tela de Matrículas)*

---

## Vídeo Demonstrativo

> [Clique aqui para assistir a demonstração](#) *(substituir pelo link do YouTube/Drive)*

---

## Exemplos de Consultas SQL

### INNER JOIN — Matrículas com Aluno e Plano
```sql
SELECT m.id_matricula, a.nome AS aluno, p.nome AS plano, p.valor, m.status
FROM matriculas m
INNER JOIN alunos a ON m.id_aluno = a.id_aluno
INNER JOIN planos p ON m.id_plano = p.id_plano
ORDER BY m.id_matricula;
```

### LEFT JOIN — Inclui Instrutor (pode ser nulo)
```sql
SELECT a.nome AS aluno, p.nome AS plano, i.nome AS instrutor, m.status
FROM matriculas m
INNER JOIN alunos      a ON m.id_aluno     = a.id_aluno
INNER JOIN planos      p ON m.id_plano     = p.id_plano
LEFT  JOIN instrutores i ON m.id_instrutor = i.id_instrutor
ORDER BY a.nome;
```

---

## Autor

**João Vitor Rodrigues Santos**  
Disciplina: Banco de Dados  
Professor: Anderson Costa  
