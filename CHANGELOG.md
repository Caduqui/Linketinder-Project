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

### Modificado

### Corrigido

### Removido
