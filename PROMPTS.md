# Prompts

Registro dos prompts usados para conduzir o agente de IA (Claude Code) no desenvolvimento com spec da feature **microsserviço de cadastro**.

Os prompts estão na ordem em que foram enviados e transcritos como foram escritos, sem correção. Em cada etapa há também as decisões respondidas às perguntas do agente e o que a etapa produziu.

## Etapa 1 — Contexto para o agente (`AGENTS.md`)

**Prompt**

> Explore o repositório e gere um AGENTS.md na raiz do projeto. Além da visão geral, stack, estrutura, como executar e testar, convenções e restrições inclua o Domínio do Linketinder: as entidades (candidato, empresa, vaga, competência, curtida, match) e como se relacionam. As Regras de negócio centrais: como funciona a curtida e quando o match acontece.Princípio do Anonimato: quais dados são visíveis antes e depois do match. Este princípio deve ser respeitado em qualquer feature. Banco de dados: onde ficam os scripts e como aplicá-los. Sempre que houver dúvidas e questões a ser tratadas é melhor me perguntar do que decidir sozinho.

**Decisões respondidas ao agente**

| Pergunta | Resposta |
| --- | --- |
| Antes do match, o que a empresa vê do candidato? | Formação, competências e descrição |
| Antes do match, o candidato vê qual empresa é dona da vaga? | Não, a empresa fica oculta |
| Depois do match, o que é revelado? | Perfil completo, exceto a senha |
| Como tratar a API REST, que devolve o cadastro completo sem anonimato? | Como pendência conhecida |

**Resultado:** `AGENTS.md`, com a regra "na dúvida, pergunte", o Princípio do Anonimato como restrição obrigatória e as pendências encontradas no código.

## Etapa 2 — Skill de desenvolvimento com spec

**Prompt**

> Eu acabei de trazer a skill de um outro projeto que fiz. A etapa de agora é a skill de desenvolvimento com spec. Adapte esta skill ao Linketinder: as specs passam a ficar em docs/specs/<nome-da-feature>/, o checklist de validação deve incluir o Princípio do Anonimato e cada tarefa deve atualizar o CHANGELOG.md.

**Decisões respondidas ao agente**

| Pergunta | Resposta |
| --- | --- |
| Em que formato cada tarefa registra a mudança no CHANGELOG? | Keep a Changelog |
| Criar o `CHANGELOG.md` junto com a adaptação da skill? | Não criar agora |

**Resultado:** `skills/feature-com-spec/SKILL.md` adaptada ao projeto.

## Etapa 3 — `CHANGELOG.md`

**Prompt**

> crie um CHANGELOG.md na raiz do projeto, seguindo o padrão Keep a Changelog.

**Resultado:** `CHANGELOG.md` com a seção `[Não lançado]` e as quatro categorias; a skill foi ajustada para usar "Modificado".

## Etapa 4 — Skill de revisão

**Prompt**

> crie uma skill que revise o código com foco na aderência à spec. Registre os apontamentos e o que você corrigiu.

**Decisões respondidas ao agente**

| Pergunta | Resposta |
| --- | --- |
| Onde registrar os apontamentos e as correções? | Em `docs/specs/<feature>/revisao.md` |

**Resultado:** `skills/revisao-com-spec/SKILL.md`. O uso da skill ficou para depois da implementação da feature.

## Etapa 5 — Especificação (`docs/specs/microservico/spec.md`)

**Prompt**

> Vamos para a etapa de especificação (docs/specs/microservico/spec.md). Use a sua skill para escrever a spec.
>
> Seu desafio é implementar a parte de cadastro de empresa/candidato em forma de um microserviço sem o uso de frameworks.
>
> Imagine o microsserviço como uma nova aplicação, um programa a parte. Você pode implementá-lo no mesmo diretório do projeto de forma desacoplada e em backend. Com isso, executar esse serviço em outra instância do InteliJ, de forma que ele fique disponível em alguma porta. E pesquisar uma forma de criar os endpoints para integração do frontend com esse microsserviço.
>
> Requisitos:
>
> Implementar um microsserviço somente de cadastro sem framework;
> Explorar uma solução para criar a comunicação entre frontend e microsserviço para o cadastro;
> Faça descrições no readme.md do que foi explorado sobre a integração.
> Ser possível cadastrar candidatos ou empresas por meio do microsserviço criado.
>
> Faça-me perguntas antes de escrever, revise e valide comigo

**Decisões respondidas ao agente, antes da primeira versão**

| Pergunta | Resposta |
| --- | --- |
| Como o microsserviço fica em relação ao projeto principal? | Pasta com build próprio |
| "Sem framework": qual servidor HTTP? | `HttpServer` do JDK |
| Como distinguir candidato de empresa? | Uma rota por perfil |
| O que fazer com os `POST` de cadastro da API REST atual? | Ficam como estão |
| O que responder a um cadastro repetido? | `409` com mensagem genérica |
| Quais campos são exigidos do candidato? | Os mesmos da API atual |
| Até onde vai a validação? | Senha, data e formatos |
| A senha é gravada como? | Como hoje; o texto puro vira pendência |

Nessa mesma rodada foram respondidas quatro perguntas cujas respostas mudaram depois, pelos prompts de ajuste abaixo: como entram as competências, o que o formulário do site faz após o cadastro, a solução de integração com o frontend e onde fica a tela de competências.

**Prompts de ajuste, na ordem**

> Integrar o frondend com o backend sem framework será nossa próxima feature, então foque em apenas o microservico nesta feature. Sobre a P-02 deixe como estava igual o CLI mesmo, a P-06 existe validações que verifica competencias com o mesmo nome, compara duplicatas em minusculo. As competências devem ficar junto com o cadastro do candidato e da vaga mesmo

> Essa feature é para implementar um microserviço somente de cadastro sem framework e a comunicação entre frontend e microsservico para o cadastro é apenas para explorar uma solução e não para implementar, como dito anteriormente: Imagine o microsserviço como uma nova aplicação, um programa a parte. Você pode implementá-lo no mesmo diretório do projeto Linketinder de forma desacoplada e em backend. Com isso, executar esse serviço em outra instância do InteliJ, de forma que ele fique disponível em alguma porta. Sobre a dúvida 2. Passa a ignorar maiusculas também no banco. Agora entendi a pergunta 3. Implemente apenas o cadastro candidatos/empresas e realmente competencias pode ficar separado. Sobre 4. estão confirmadas.

> P-01 O cadastro deve cadastrar todas as informações do candidato e da empresa. Mas só após o cadastro é que o candidato vai criar e escolher as competências dele. A P-05 está certo.

> Vamos deixar as competencias para a proxima feature então. P-02 como eu disse, não vai ter integração com o front-end nessa feature. P-03 a ideia é o candidato poder alterar suas competências a qualquer momeneto mas vamos deixar isso para outra feature, deixe como pendencia. P-04 Deixe como uma pendencia para o futuro. P-06 pode ser assim

> É interessante para a competencia após cadastrar o candidato, aparecer para ele escrever as competencias para ser salvo no banco

> Opção A

> 2. O candidato é obrigado a ter ao menos 1 competencia. 1. Está correto

> Aceito e a spec está aprovada

**Resultado:** `docs/specs/microservico/spec.md` aprovada. O escopo final ficou em três rotas (`POST /candidatos`, `POST /empresas` e `POST /candidatos/{id}/competencias`), sem alteração no frontend e com a integração apenas explorada.

## Etapa 6 — Plano de implementação (`docs/specs/microservico/plano.md`)

O plano foi escrito pelo agente logo após a aprovação da spec, seguindo a skill. Os prompts desta etapa foram a regra abaixo e as respostas às pendências do plano.

**Prompt**

> Um detalhe que deixei passar Use dados fictícios. Em scripts de carga e testes, nunca use CPFs, e-mails ou nomes reais.
> Também nunca faça commit de credenciais. Senhas de banco, chaves de API e arquivos .env devem ficar fora do repositório.

**Prompts de resposta às pendências, na ordem**

> 1. sim 2. não precisa do .env por enquanto, 3. Vou deixar você commitar depois. 4. Essas alterações .idea e frontend não é para mexer e nem commitar. 5. Ficam

> 1. sim 2. sim

**Resultado:** as duas regras entraram em "Restrições" do `AGENTS.md`; a spec passou a exigir a senha do banco só por variável de ambiente e dados fictícios nos testes; `docs/specs/microservico/plano.md` aprovado, com 8 tarefas.

## Etapa 7 — Implementação incremental

**Prompt**

> Implemente uma tarefa por vez e para cada tarefa:
>
> Rode a aplicação e os testes.
> Confira se o anonimato continua preservado.
> atualize o CHANGELOG.md e marque a tarefa como concluída no plano.
> Faça um commit semântico por tarefa.
> Se a spec se mostrar incompleta durante a implementação, atualize a spec primeiro e só depois o código.

**Resultado:** as 8 tarefas do plano, uma por commit.

| Tarefa | Mensagem do commit |
| --- | --- |
| 1 | `feat: adiciona esqueleto do microsservico de cadastro` |
| 2 | `feat: adiciona validacao e regras de cadastro no microsservico` |
| 3 | `feat: adiciona rotas de cadastro de candidato e empresa no microsservico` |
| 4 | `feat: grava cadastros do microsservico no PostgreSQL` |
| 5 | `feat: adiciona regras e rota de competencias do candidato no microsservico` |
| 6 | `feat: grava competencias do candidato no PostgreSQL em transacao` |
| 7 | `docs: descreve o microsservico de cadastro e a exploracao da integracao no README` |
| 8 | `docs: registra o microsservico de cadastro no AGENTS.md e fecha a spec` |

## Etapa 8 — Este arquivo

**Prompt**

> Crie um PROMTPS.md que mostre todos os comandos solicitados para o agente.

**Resultado:** `PROMPTS.md`.

## O que funcionou nos prompts

- **Pedir perguntas antes da escrita.** "Faça-me perguntas antes de escrever, revise e valide comigo" fez as decisões aparecerem antes do código. A spec passou por várias rodadas de ajuste e o plano foi escrito uma vez só.
- **Dar a regra e o motivo.** O Princípio do Anonimato foi pedido como "restrição explícita, pois o agente precisa respeitá-la em qualquer feature", e passou a aparecer na spec, em cada tarefa e no checklist.
- **Dizer o que fica para depois.** Frases como "vamos deixar isso para outra feature, deixe como pendencia" reduziram o escopo sem perder a ideia: as pendências ficaram registradas na spec.
- **Definir o ciclo de cada tarefa.** A lista da etapa 7 (rodar, conferir o anonimato, atualizar o CHANGELOG, marcar no plano, commitar) virou o roteiro repetido nas 8 tarefas.

## O que deu trabalho

- **Termos resumidos demais do lado do agente.** As premissas "P-02", "P-03" e "P-06" precisaram ser reexplicadas com exemplos antes de serem decididas.
- **Uma mesma frase com duas leituras.** "Competências pode ficar separado" podia ser uma rota separada ou outra feature; a decisão sobre as competências mudou quatro vezes até fechar.
- **Regras lembradas no meio do caminho.** Dados fictícios e credenciais entraram depois da spec aprovada e obrigaram a rever uma decisão já tomada (a senha do banco com valor padrão).
