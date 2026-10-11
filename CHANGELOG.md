# Changelog

Todas as mudanças relevantes do Linketinder são registradas neste arquivo.

O formato segue o [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/).

## [Não lançado]

### Adicionado
- Microsserviço de cadastro: aplicação independente em `microservico-cadastro/`,
  com servidor HTTP do JDK na porta 8081
  (spec `microservico`, tarefa 1).
- Validação e regras de cadastro de candidato e empresa no microsserviço
  (spec `microservico`, tarefa 2).
- Rotas `POST /candidatos` e `POST /empresas` do microsserviço, com respostas
  em JSON sem a senha (spec `microservico`, tarefa 3).
- Cadastro de candidatos e empresas gravado no PostgreSQL pelo microsserviço;
  cadastro repetido responde `409` e corpo fora de UTF-8 responde `400`
  (spec `microservico`, tarefa 4).
- Regras e rota `POST /candidatos/{id}/competencias` do microsserviço:
  competências somadas às do candidato, reaproveitando as existentes sem
  diferenciar maiúsculas (spec `microservico`, tarefa 5).
- Competências do candidato gravadas no PostgreSQL pelo microsserviço, em
  transação (spec `microservico`, tarefa 6).
- README descreve o microsserviço de cadastro e a exploração da comunicação
  com o frontend (spec `microservico`, tarefa 7).

### Modificado
- `AGENTS.md` passa a descrever o microsserviço de cadastro e suas pendências
  (spec `microservico`, tarefa 8).

### Corrigido

### Removido
