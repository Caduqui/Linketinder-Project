autor: Guilherme Lima Conte

**Como executar:**

# Na raiz do projeto, execute:

`groovy -cp src/main/groovy src/main/groovy/org/example/Main.groovy`

Caso esteja utilizando IntelliJ, é possível executar o projeto apertando o botão de play enquanto está no diretório Main.groovy.

# Informações do projeto:

Optei por explorar o máximo que dava dos benefícios que o groovy proporciona. Para deixar o mais limpo possível não utilizei tipo de visibilidade nas classes, atributos, métodos e construtores e também deixei a maioria dos tipos como def, com exceção das competências que tem como tipo uma lista genérica de strings.
Utilizei do conceito de "Groovy Truth" do curso da udemy no método formatarCompetencias, como listas vazias já possuem valor false me permitiu já iniciar o operador ternário validando a verdade.

É possível adicionar novos candidatos e novas empresas, não foi feito nenhuma validação para confirmar se os tipos estão corretos e no modelo que deve ser seguido no cadastro, mas já é possível.

Devido a diversidade que eu quis deixar nas empresas, não deixei as competências já pré-determinadas para quem vai fazer o cadastro, possibilitando ser qualquer nova competência.

# Front-end

Foi implementando a listagem dos candidatos disponíveis na visão da empresa e as vagas disponíveis na visão do candidato, mostrando todas as informações do candidato/vaga deixando anônimo apenas o nome.
Foi adicionado um cadastro ao lado, da empresa e do candidato.
Por fim da listagem dos candidatos disponíveis há um gráfico em barras adicionado usando a biblioteca chart.js que indica o número de candidatos por competências

# Banco de dados

O banco de dados foi desenvolvido utilizando PostgreSQL, graças a ele pude observar um problema crítico em meu projeto que não considerava Empresa e Vaga como classes diferentes, ccom o desenvolvimento do banco de dados, pude perceber esse erro e corrigi-lo. O arquivo localizado em `database/Linketinder.sql` contém todas as criações, inserções e busca realizadas desde sua criação.
Inicialmente o diagrama entidade relacionamento foi feito no site dbdiagram.io, depois realizei o export em Postgre e continuei no pgadmin4.

Para o Desafio ZG foi implementado a sincronização do Groovy com o banco de dados com a criação das classes dentro do diretório `dao`, foi implementado o CRUD das 4 classes exigidas: Candidato, Empresa, Competencia, Vaga. Além de algumas verificações e buscar a fim de evitar uma possível quebra no banco de dados.

![der do Linketinder](assets/der.png)

# Match e Curtida

Foi implementado duas novas classes Match e Curtida para que a mecânica de um candidato curtir uma vaga e a empresa responsável por aquela vaga curtir o candidato gere um Match para candidato-vaga. Aproveitei o fato de já ter implementado o banco de dados e criei um novo método em VagaDAO para listar todas as vagas daquela empresa e usar dessa informação durante o menu para quando estivermos sendo a "empresa" aparecer todas as vagas que aquela empresa possui.

# Índice de afinidade

Foi adicionado na Visão do Candidato o índice de afinidade que o candidato possui com aquela vaga com base nas competências do candidato/vaga. O cálculo é feito com a quantidade de competências que o candidato tem em comum com a vaga dividido pela quantidade total de competências da vaga.
Foi adicionado também durante a produção do índice um select para simularmos melhor a "experiência" que seria estar na visão naquele candidato. Por ora será assim até a implementação do sistema de login.

# Inserção da lógica de curtida no banco de dados

Foi criado uma tabela de curtida referente ao candidato e uma tabela de curtida referente a empresa/vaga. O match é realizado quando as duas tabelas de curtidas são validadas. Para isso acontecer adicionei uma restrição em vagas que valida se aquela vaga está relacionado com a empresa que iria curtir o candidato, algo que não pode acontecer. Por fim, inserimos algumas curtidas e inserimos a lógica do match e logo depois verificamos isso no `SELECT`. 

# MVP do match

Como já existia a lógica do match devido a inserção da lógica de curtida no bd, nada mais prático do que eu criar uma classe Match que se comunica com o bd também. Devido desde antes eu já ter feito um código para mostrar se o match ocorreu e a listagem de matches, eu aproveitei de uns trecho do código antigo, assim não tendo alteração na main sobre a listagem dos matches.

# Clean code

Agora com a curtidaDAO, a classe Curtida é desnecessário.
Alterei as variáveis que ainda estavam com o tipo def. Entre elas estava a dataNascimento. Agora é do tipo LocalDate e possui um método em Cadastro chamado lerDataNascimento().
estaEmUso era um nome de método que não dizia claramente o que fazia, troquei para estaVinculadoACandidatoOuVaga
Para manter o padrão exibirMatch se tornou exibirDados também.
Coloquei duas variáveis estáticas e constantes para representar o tamanho da senha na hora de cadastrar e o id na main.

Refatorei todas as classes DAO, agora ConexaoBanco tem o método executar que já facilita a abertura e fechamento das conexões, reduzindo muitas linhas repetidas nos DAO. 

Refatorei também as classes Cadastro e Main. Agora Cadastro foi divido em 7 novas classes. A ideia era somente diminuir a classe Cadastro que estava enorme, mas ao fazer essas mudanças notei que alguns métodos estavam muito grandes e fiz a criação de novos, como selecionarCompetencia, selecionaCandidato, etc.. lerDadosVaga, lerDadosEmpresa, etc.. Dessa forma consegui reduzir algumas classes quase pela metade e outras até mais que a metade.

Agora com a classe Cadastro refatorado, foi possível diminuir a main também. Pois agora não possui mais menuCandidato, menuEmpresa, etc.. Também apliquei os testes que haviam deixado de funcionar devido as mudanças do código.

No front-end eu separei todas as funcionalidades que estavam inseridas no main. Agora tem 6 novas classes divididas que fazem a mesma funcionalidade de antes.

Adicionei 6 novas interfaces localizadas no pacote `org.example/repositorio` que são responsáveis em dizer quais operações existem sobre um candidato, vaga, empresa.. Agora uma classe de Cadastro não conhece uma classe que abre conexão com o banco de dados, ela depende da abstração e não de uma classe DAO.
Isso resulta que agora quem instância o DAO é o Main, ele é a única classe que conhece as classes concretas. Em consequência disso agora é possível passar `CandidatoRepositorio` no lugar de `CandidatoDAO` como parâmetro de um Mock. Ou seja, os testes unitários rodam sem banco nenhum.

# Design Pattern

## Factory

ConexaoBanco estava tendo muitas funcionalidades, ele sabia qual banco usar, abria a conexão e executava as consultas, por isso eu separei essas funcionalidades em 5 classes novas aplicando o padrão Factory. O TipoBanco lê da variável de ambiente LINKETINDER_BANCO qual banco usar. 
A interface ProvedorConexao define o contrato de abrir uma conexão, e cada banco tem sua implementação (ProvedorPostgreSQL, ProvedorMySQL). 
E a FactoryConexao recebe o tipo e cria o provedor correto. Assim, trocar de banco não exige mudar o código, só a variável. Agora um banco novo é só mais um provedor.

## Singleton

Antes, o `ConexaoBanco` abria uma conexão nova a cada consulta e a fechava no final. Nas listagens isso se multiplicava, pois cada item faz as próprias consultas internas.
Apliquei o Singleton no `ConexaoBanco` para existir uma única instância dele na aplicação. O construtor é private, então ninguém cria outra com new e a instância fica num campo private static e é obtida por `ConexaoBanco.obterInstancia()`, criada só no primeiro uso. Medindo no Postgre, listar candidatos abria 6 conexões, listar vagas 11, listar matches 13 e agora cada uma usa uma conexão só.
Essa instância abre a conexão uma única vez e a reaproveita em todas as consultas. Antes de usar, `isValid()` confere se ela ainda está ativa e, se o banco tiver reiniciado, uma nova é aberta automaticamente. E ao sair, o Main chama fechar(). Os DAOs continuam recebendo `ConexaoBanco` pelo construtor, no entanto, somente a `Main` chama `obterInstancia()`. Assim o Singleton garante uma instância única sem que cada classe dependa de um método estático.


# MVC

As 7 classes base do projeto foram para o pacote `model`

Os modelos pararam de imprimir `exibirDados()`, agora virou `descrever()` que retorna o texto em vez de imprimir. Pois, agora quem faz a função de imprimir é a view. A leitura do teclado e a impressão saíram das classes de cadastro e foram para as views.

As regras de negócio foram para o service que está entre os controllers e os repositórios.

Quando uma regra recusa a operação, o service lança `RegraDeNegocioException` com uma mensagem, onde o `MenuController` captura e manda a view exibir

O que sobrou das classes de cadastro virou os controllers, que pedem os dados à view, entregam ao service e mandam a view responder. 

O `MenuController` assumiu os menus da `Main` e `CadastroCrud` se tornou `ControllerCrud` implementada pelos quatro controllers de CRUD. 

E agora a `Main` ficou só com a montagem das dependências.



# API REST

Devido a construção do código aplicando os princípios SOLID e MVC foi possível implementar a lógica de API sem precisar alterar os códigos já existentes, somente nas classes `Provedor` eu tive que colocar o driver como uma constante pois, o driver não se registrava sozinho, mas `DriverManager` já havia sido inicializado pelo TomCat antes da aplicação subir e a varredura pelos drivers já havia acontecido

## Contrato

Base: `http://localhost:8080/linketinder`

Temos 10 endpoints, todos sendo POST e GET:
POST em `/candidatos` cadastra um candidato, se houver sucesso lança 201 + `Location` e se der erro apresenta 400
GET em `/candidatos` lista todos os candidatos, se houver sucesso lança 200 e se der erro apresenta 500
GET em `/candidatos/{id}` busca um candidato, se houver sucesso lança 200 e se der erro apresenta 400 se o id não for número e 404 se não existir
POST em `/empresas` cadastra uma empresa, se houver sucesso lança 201 + `Location` e se der erro apresenta 400
GET em `/empresas` lista todas as empresas, se houver sucesso lança 200 e se der erro apresenta 500
GET em `/empresas/{id}` busca uma empresa, se houver sucesso lança 200 e se der erro apresenta 400 se o id não for número e 404 se não existir
POST em `/vagas` cadastra uma vaga, se houver sucesso lança 201 + `Location` e se der erro apresenta 400
GET em `/vagas` lista todas as vagas, se houver sucesso lança 200 e se der erro apresenta 500
GET em `/vagas?idEmpresa={id}` lista todas as vagas de uma empresa, se houver sucesso lança 200 e se der erro apresenta 400
GET em `/vagas/{id}` busca uma vaga, se houver sucesso lança 200 e se houver erro apresenta 400 se o id não for número e 404 se não existir

Qualquer outra requisição como DELETE, PUT é respondido com erro 405 e qualquer requisição pode dar erro 500 caso o banco falhe.

## Recursos utilizados

`javax.servlet.api` versão 4.0.1 para compilar o servlet
Apache Tomcat versão 9.0.122 para servir como servidor que executa os servlets
`groovy-json` versão 5.0.8 
`JsonSlurper` e `JsonOutput`

O servlet recebe um `HttpServletRequest`, escreve em uma `HttpServletResponde` e o Tomcat cuida das portase threads.

No `build.gradle` tive que adicionar esses recursos então agora

`plugins` passou a ter od id `groovy` `application` e `war`

e nas dependências:

```groovy
dependencies {
    compileOnly 'javax.servlet:javax.servlet-api:4.0.1'
    testImplementation 'javax.servlet:javax.servlet-api:4.0.1'
    implementation 'org.apache.groovy:groovy-json:5.0.8'
}
```

É necessário `compileOnly` no servlet-api pois quem fornece essa biblioteca em tempo de execução é o Tomcat, então se ela fosse junto no WAR, haveria duas cópias da mesma classe e o Tomcat recusaria a aplicação.

Já o driver do PostGreSQL e o Groovy precisam ir no WAR (`implementation), porque o Tomcat não os tem.

`compileOnly` não vale para os testes. Como os specs criam mocks de `HttpServletRequest`, e o servlet-api precisa estar no classpath de teste então utilizei `testImplementation` porque o Spock precisa da classe em tempo de execução para gerar o mock. Ele também não entra no WAR, porque o `war` empacota `runtimeClasspath`

## endpoints

Todas as classes servlets tem o caminho declarado com @WebServlet pois o Tomcat varre as anotações do WAR ao subir a aplicação.

`HttpServlet` chama `doGet` para requisição GET e `doPost` para requisição POST, as demais requisições responde com 405.

## observação

O `POST` aceita o campo `senha` para o cadastro mas `Json.deCandidato` e `Json.deEmpresa` não o incluem, dessa forma um cliente que lista candidatos nunca vai receber as senhas.


## como rodar

1. Baixe o **Tomcat 9** e descompacte
2. Gere o WAR `./gradlew war` -> `build/libs/linketinder.war`
3. Copie o WAR para `apache-tomcat-9.0.122/webapps`
4. Suba o servidor: `bin/startup.sh`
5. O banco de dados é necessário estar rodando, caso contrário aparecerá erro 500 ao executar o passo 6.
6. Entre em `curl http://localhost:8080/linketinder/candidatos`