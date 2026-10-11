# AGENTS.md — Linketinder

Guia para agentes de IA que trabalham neste repositório. Leia inteiro antes de alterar código.

## Regra nº 1: na dúvida, pergunte

Sempre que houver dúvida, ambiguidade ou uma decisão que não esteja escrita aqui (regra de negócio, mudança de schema, nova dependência, contrato da API, o que fazer com uma pendência listada abaixo), **pare e pergunte ao autor antes de decidir sozinho**. Explique o que encontrou e quais são as opções. Não "resolva" silenciosamente uma inconsistência entre este documento e o código.

## Visão geral

Linketinder é um projeto de LinkedIn com Tinder em que **candidatos curtem vagas**, **empresas curtem candidatos** e, quando a curtida é recíproca, acontece um **match**. Até o match, as partes não sabem quem está do outro lado (ver [Princípio do Anonimato](#princípio-do-anonimato-restrição-obrigatória)).

O repositório tem três partes que hoje **não estão integradas entre si**:

| Parte | O que é | Persistência |
|---|---|---|
| Backend CLI (`src/main/groovy`, `Main.groovy`) | Menus no terminal com CRUD, curtidas e matches | PostgreSQL |
| API REST (`org.example.api`) | Servlets para candidatos, empresas e vagas (só `GET` e `POST`) | PostgreSQL |
| Frontend (`frontend/`) | Páginas estáticas em TypeScript | `localStorage` do navegador (não chama a API) |

Curtida e match existem apenas no CLI e no banco; a API e o frontend ainda não têm esses fluxos.

## Stack

- **Backend:** Groovy 5.0.8 sobre JDK 25, Gradle 9.6 (wrapper), plugins `groovy`, `application`, `war`.
- **Banco:** PostgreSQL via JDBC puro (`org.postgresql:postgresql:42.7.7`), sem ORM e sem ferramenta de migração.
- **API:** `javax.servlet-api` 4.0.1 (`compileOnly`), implantada como WAR no **Tomcat 9** (não usar `jakarta.*` / Tomcat 10+); JSON com `groovy-json`.
- **Testes:** Spock 2.4 (`groovy-5.0`) em JUnit Platform.
- **Frontend:** TypeScript ~6.0 + Vite 8, Chart.js (carregado por CDN em `empresa.html`), HTML/CSS sem framework.

## Estrutura

```
build.gradle, settings.gradle, gradlew*   build do backend
database/Linketinder.sql                  schema + carga inicial + consultas de exemplo
assets/der.png                            diagrama entidade-relacionamento
lib/                                      jars do Groovy 6.0.0-beta (NÃO usados pelo Gradle; não mexer sem perguntar)
src/main/groovy/org/example/
  Main.groovy        monta as dependências do CLI (único lugar que instancia DAOs)
  Banco/             ConexaoBanco (Singleton), FactoryConexao, ProvedorConexao + PostgreSQL/MySQL, TipoBanco
  model/             Candidato, Empresa (ambos TipoPessoa), Vaga, Competencia, Match
  repositorio/       interfaces de persistência (uma por agregado)
  dao/               implementações JDBC das interfaces de repositorio
  service/           regras de negócio + RegraDeNegocioException
  controller/        orquestram view <-> service; MenuController navega os menus
  view/              única camada que lê teclado e imprime (EntradaDados, *View)
  api/               ApiServlet (base), *Servlet, Json (conversão), Dependencias, EncerradorDeConexao
src/test/groovy/org/example/              specs Spock espelhando os pacotes de main
frontend/
  index.html, candidato.html, empresa.html, vaga.html, style.css
  ts/  main.ts, modelos.ts, dados.ts, formularios.ts, telaCandidato.ts, telaEmpresa.ts, telaVaga.ts
```

Fluxo das camadas: `view` ⇄ `controller` → `service` → `repositorio` (interface) ← `dao` → `Banco`.

## Domínio

| Entidade | Tabela | Campos principais | Relacionamentos |
|---|---|---|---|
| **Candidato** | `candidatos` | nome, sobrenome, data de nascimento, e-mail (único), CPF (único), país, CEP, descrição, senha, formação | N:N com Competência (`candidato_competencia`) |
| **Empresa** | `empresas` | nome, e-mail (único), CNPJ (único), país, CEP, descrição, senha | 1:N com Vaga |
| **Vaga** | `vagas` | nome, descrição, estado (UF, 2 letras), cidade, `id_empresa` | pertence a uma Empresa; N:N com Competência (`vaga_competencia`) |
| **Competência** | `competencias` | nome (único) | ligada a candidatos e vagas |
| **Curtida** | `curtida_candidato_vaga`, `curtida_empresa_candidato` | ver abaixo | não tem classe de modelo; existe só nas tabelas e em `CurtidaRepositorio` |
| **Match** | `matches` | `id_candidato`, `id_empresa`, `id_vaga` (trio único) | liga um Candidato a uma Vaga e à Empresa dona dela |

Detalhes que costumam confundir:

- Empresa e Vaga são entidades diferentes: quem publica é a empresa, quem é curtida pelo candidato é a **vaga**.
- Em `Candidato` e `Vaga` as competências são `List<String>` com os **nomes**; os DAOs resolvem para `competencias` com `buscarOuInserir`, criando a competência se ela ainda não existir.
- No backend `Vaga` guarda o objeto `Empresa`; no frontend guarda apenas `idEmpresa`.

## Regras de negócio centrais

### Curtida

- **Candidato → Vaga:** grava `(id_candidato, id_vaga)` em `curtida_candidato_vaga`.
- **Empresa → Candidato, para uma vaga específica dela:** grava `(id_empresa, id_candidato, id_vaga)` em `curtida_empresa_candidato`. A empresa só pode curtir usando uma vaga própria: o CLI valida com `VagaService.buscarVagaDaEmpresa` e o banco garante com a FK composta `(id_empresa, id_vaga) → vagas(id_empresa, id)`.
- Curtidas são únicas. Repetir uma curtida não gera erro: o `INSERT ... ON CONFLICT DO NOTHING` não insere nada, o repositório devolve `false` e a view avisa que já foi curtido.
- Não existe "descurtir".

### Match

- O match acontece quando, **para a mesma vaga**, o candidato curtiu a vaga **e** a empresa dona da vaga curtiu esse candidato. A ordem das curtidas não importa.
- Após cada curtida nova, `CurtidaController.informarMatch` chama `CurtidaService.houveMatch`, que delega a `MatchRepositorio.criarMatchSePossivel`: um `INSERT ... SELECT` em `matches` que só insere se as duas curtidas existirem e a vaga for da empresa. Devolve `true` apenas quando o match foi criado naquele momento.
- O match é do trio candidato–empresa–vaga: o mesmo candidato pode ter vários matches com a mesma empresa, um por vaga.

### Outras regras

- Empresa com vagas cadastradas não pode ser excluída (`EmpresaService.excluir`).
- Competência vinculada a candidato ou vaga não pode ser excluída; nome de competência não pode ser vazio nem duplicado (`CompetenciaService`).
- Excluir um candidato ou uma vaga apaga antes suas competências, curtidas e matches (feito no DAO).
- Senha tem no mínimo 6 caracteres; data de nascimento no formato `AAAA-MM-DD`; estado é a sigla com 2 letras.
- Índice de afinidade (só no frontend, `calcularAfinidade`): competências em comum ÷ total de competências da vaga, em porcentagem arredondada, ignorando maiúsculas/minúsculas.
- Regra violada → o service lança `RegraDeNegocioException`; o `MenuController` exibe a mensagem no CLI e o `ApiServlet` responde `400`.

## Princípio do Anonimato (restrição obrigatória)

**Esta regra vale para qualquer feature, em qualquer camada (CLI, API, frontend, banco, logs, testes, mensagens de erro). Nenhuma mudança pode expor dados fora do que está na tabela abaixo.**

| Momento | A empresa vê do candidato | O candidato vê da vaga/empresa |
|---|---|---|
| **Antes do match** | Somente ID, formação, descrição e competências | Somente os dados da vaga: ID, nome da vaga, descrição, cidade/estado, competências e índice de afinidade. **Nada da empresa, nem o nome** |
| **Depois do match** | Perfil completo do candidato, **exceto a senha** | Perfil completo da empresa dona da vaga, **exceto a senha** |

Regras derivadas:

1. Antes do match são proibidos para a empresa: nome, sobrenome, e-mail, CPF, data de nascimento, país e CEP do candidato. Para o candidato: nome, e-mail, CNPJ, país, CEP e descrição da empresa.
2. A revelação pós-match vale apenas entre as duas partes daquele match, não para terceiros.
3. **A senha nunca é exibida nem devolvida**, em nenhum momento, por nenhuma camada (nem em JSON, nem em `descrever()`, nem em log).
4. Curtir não revela nada: uma parte não fica sabendo quem a curtiu até existir o match.
5. Ao exibir candidatos para uma empresa use a representação anônima (`Candidato.descreverAnonimo()`); nunca reutilize `descrever()` nesse contexto. Ao exibir vagas para um candidato não inclua a empresa.
6. Se uma feature parecer exigir a exposição de um dado fora da tabela (ou se não estiver claro de qual lado é a tela), **pergunte antes de implementar**.

### Pendências conhecidas (código ainda fora da regra)

Não amplie nenhuma delas, não as use como exemplo a seguir e pergunte antes de alterá-las:

- **CLI:** em `CurtidaController.candidatoCurtirVaga` as vagas são listadas com `Vaga.descrever()`, que imprime o nome da empresa antes do match.
- **API:** `GET /vagas` devolve `empresa: {id, nome}`; `GET /candidatos`, `GET /candidatos/{id}`, `GET /empresas` e `GET /empresas/{id}` devolvem o cadastro completo (sem a senha) para qualquer cliente, sem autenticação.
- **Pós-match:** `Match.descrever()` mostra só nome e e-mail das partes; a regra permite o perfil completo sem a senha.
- **Frontend:** `dados.ts` mantém todos os dados, inclusive senhas, no `localStorage`; as telas só renderizam os campos anônimos, mas os dados estão no navegador.
- **Sem login:** "quem está vendo" é simulado escolhendo um candidato/empresa (`listarNomes` no CLI, `<select>` no frontend). Essa seleção mostra nomes por ser a própria parte se identificando, não a parte contrária.

## Banco de dados

- **Scripts:** um único arquivo, `database/Linketinder.sql`, na ordem: criação das tabelas base → carga inicial (competências, candidatos, empresas, vagas, vínculos) → consultas de exemplo → tabelas de curtida e match → constraint empresa/vaga → curtidas e matches de exemplo. O DER está em `assets/der.png`.
- **Conexão esperada pelo código** (`ProvedorPostgreSQL`): `jdbc:postgresql://localhost:5432/linketinder`, com usuário e senha fixos no código-fonte (ver as pendências de dados e credenciais, em [Restrições](#restrições)).
- **Como aplicar:** o script não cria o banco e não é idempotente (sem `DROP`/`IF NOT EXISTS`), então rode uma única vez em um banco vazio:

  ```bash
  createdb -U postgres linketinder
  psql -U postgres -d linketinder -f database/Linketinder.sql
  ```

  Sem `psql` no PATH, abra o arquivo no pgAdmin ou no console SQL do IntelliJ (data source em `.idea/dataSources.xml`) e execute.
- **Problema conhecido no script:** o último `SELECT` antes do bloco `-- curtidas` (linhas 309–315) está sem `;`. Executado de uma vez, o `CREATE TABLE "curtida_candidato_vaga"` falha junto com ele e tudo que depende dessa tabela também. Até ser corrigido, execute o bloco de curtidas/matches separadamente. Pergunte antes de corrigir o arquivo.
- **Escolha do banco:** a variável de ambiente `LINKETINDER_BANCO` (`POSTGRESQL` padrão, ou `MYSQL`) define o provedor via `FactoryConexao`. Na prática só o PostgreSQL funciona: não há driver MySQL no `build.gradle` e os DAOs usam SQL específico do PostgreSQL (`RETURNING`, `ON CONFLICT`).
- **Mudanças de schema:** não há migrações. Qualquer alteração precisa ser refletida em `database/Linketinder.sql`, nos DAOs e no DER — **pergunte antes de alterar o schema**.

## Como executar

Todos os comandos do backend partem da raiz do repositório; no Windows use `gradlew.bat` (ou `./gradlew` no Git Bash).

- **CLI:** exige o PostgreSQL no ar com o script aplicado. Rode `org.example.Main` pelo IntelliJ. `./gradlew run` **não funciona hoje** (falta `application.mainClass` no `build.gradle`); o comando `groovy -cp ...` do README depende de Groovy instalado e do driver JDBC no classpath.
- **API REST:**
  1. `./gradlew war` gera `build/libs/linketinder.war`;
  2. copie para `webapps/` de um Tomcat 9 e suba o servidor (`bin/startup.sh` ou `bin\startup.bat`);
  3. base: `http://localhost:8080/linketinder` — `GET`/`POST` em `/candidatos`, `/empresas`, `/vagas`, `GET` em `/{recurso}/{id}` e `GET /vagas?idEmpresa={id}`. Demais métodos respondem `405`. O contrato completo está no `README.md`.
- **Frontend:**

  ```bash
  cd frontend
  npm install
  npm run dev        # servidor Vite
  npx tsc --noEmit   # checagem de tipos
  ```

## Como testar

```bash
./gradlew test                                                   # suíte inteira
./gradlew test --tests "org.example.service.CurtidaServiceSpec"  # uma spec
```

- Os testes **não precisam de banco**: services e controllers recebem `Mock` das interfaces de `repositorio`; servlets recebem `Dependencias` montadas com mocks.
- Utilitários de teste: `EntradaFalsa.comRespostas([...])` simula o teclado e `SaidaConsole.capturar { ... }` captura o que a view imprimiu.
- Os DAOs e `ConexaoBanco` não têm testes automatizados (dependem de banco real); mudanças neles precisam de verificação manual com o PostgreSQL.
- O frontend não tem testes unitários e não precisa ter (dispensado pelo desafio); valide com `npx tsc --noEmit` e manualmente no navegador.
- Toda mudança de comportamento no backend deve vir com spec nova ou ajustada, e a suíte deve passar antes de considerar a tarefa concluída.

## Convenções

- **Idioma:** tudo em português — classes, métodos, variáveis, mensagens e nomes de teste (`def "candidatoCurtirVaga informa quando a curtida é repetida"()`).
- **Clean Code** é requisito do projeto: nomes autoexplicativos, funções pequenas e com uma responsabilidade, DRY, comentário só quando estritamente necessário, tratamento de erros explícito.
- **Camadas (MVC):**
  - `model` não imprime: expõe `descrever()` (e variantes) que **retornam** texto;
  - `view` é a única camada com `println` e leitura de entrada;
  - `controller` não tem regra de negócio nem SQL;
  - `service` concentra as regras e lança `RegraDeNegocioException`;
  - `dao` só persiste e implementa uma interface de `repositorio`.
- **Dependências por construtor**, apontando para interfaces. Apenas `Main` (CLI) e `api/Dependencias` (API) conhecem classes concretas e chamam `ConexaoBanco.obterInstancia()`.
- **SQL:** sempre por `conexaoBanco.executar(sql) { PreparedStatement statement -> ... }` com parâmetros `?`; nunca concatene valor de usuário na query.
- **Estilo Groovy:** tipos explícitos em vez de `def`; sem modificador de visibilidade em classes e métodos públicos; dependências como `private final`; constantes `static final` em `MAIUSCULAS`; indentação de 4 espaços; cabeçalho `@author Guilherme Lima Conte` nas classes.
- **API:** servlets estendem `ApiServlet` e usam `responder*`/`lerCorpo`; a conversão de e para JSON fica em `Json` (`paraX` lê, `deX` escreve — e nunca inclui `senha`).
- **Frontend:** um módulo por tela em `ts/`, funções exportadas com nomes em português, tipos em `modelos.ts`, dados em `dados.ts`.

## Restrições

- Respeite o [Princípio do Anonimato](#princípio-do-anonimato-restrição-obrigatória) em toda feature.
- Nunca exponha senha em saída, JSON ou log.
- Não altere o schema, o contrato da API, as dependências do `build.gradle`/`package.json` nem a pasta `lib/` sem perguntar.
- Mantenha `javax.servlet` + Tomcat 9; não migre para `jakarta` sem pedido explícito.
- Não quebre a separação de camadas (ex.: `println` fora de `view`, SQL fora de `dao`, `new XxxDAO` fora de `Main`/`Dependencias`).
- **Use dados fictícios.** Em scripts de carga e testes, nunca use CPFs, e-mails ou nomes reais. O mesmo vale para exemplos na documentação e para cadastros feitos em conferência manual.
- **Nunca faça commit de credenciais.** Senhas de banco, chaves de API e arquivos `.env` devem ficar fora do repositório: leia de variável de ambiente e não escreva o valor em código, teste, script ou documentação.
- Não faça commit nem push por conta própria.
- Encontrou inconsistência, pendência ou algo não coberto aqui? Volte à [Regra nº 1](#regra-nº-1-na-dúvida-pergunte).

### Pendências conhecidas (dados e credenciais)

Código já commitado que ainda está fora das duas regras acima. Não amplie, não use como modelo e pergunte antes de corrigir:

- `ProvedorPostgreSQL` e `ProvedorMySQL` trazem usuário e senha de banco fixos no código-fonte.
- A carga inicial (`database/Linketinder.sql` e `frontend/ts/dados.ts`) e várias specs em `src/test/groovy` usam e-mails em domínio real (`@gmail.com`), que podem pertencer a alguém.
- O `.gitignore` da raiz não ignora arquivos `.env`.
