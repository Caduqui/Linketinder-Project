package org.example

import spock.lang.Specification
import spock.lang.Unroll

class CandidatoSpec extends Specification{
    def "O construtor preenche corretamente todos os campos"() {
        given:
        def competencias = ["Java", "SQL"]
        println "Competências definidas como ${competencias}."

        when:
        println "Criando uma nova instância de Candidato"
        def candidato = new Candidato("Ana", "ana@gmail.com", "111.111.111-11", 23, "SP", "23134-201", "Dev back-end", competencias)

        then:
        candidato.nome == "Ana"
        candidato.email == "ana@gmail.com"
        candidato.cpf == "111.111.111-11"
        candidato.idade == 23
        candidato.estado == "SP"
        candidato.cep == "23134-201"
        candidato.descricao == "Dev back-end"
        candidato.competencias == competencias
        println "Todos os atributos do construtor foram preenchidos na classe Candidato."
    }

    def "Candidato é uma instância de Pessoa e TipoPessoa"() {
        expect:
        println "Checando se Candidato implementa a interface Pessoa e estende TipoPessoa"
        new Candidato("Ana", "a@x.com", "111", 20, "SP", "0", "d", []) instanceof Pessoa
        new Candidato("Ana", "a@x.com", "111", 20, "SP", "0", "d", []) instanceof TipoPessoa
        println "A classe Candidato herda corretamente"
    }

    @Unroll
    def "formatarCompetencias('#competencias') retorna '#esperado'"() {
        given:
        def candidato = new Candidato("Bruno", "b@x.com", "222", 30, "RJ", "20000", "desc", competencias)
        println "Testando formatação para as competências: ${competencias}"

        expect:
        candidato.formatarCompetencias() == esperado
        println "O método processou e retornou a string exata: '${esperado}'."

        where:
        competencias | esperado
        ["Java", "Docker"] | "Java, Docker"
        ["Python"] | "Python"
        [] | "Nenhuma competência cadastrada"
    }

    def "exibirDados imprime as informações do candidato"() {
        given:
        println "Criando candidato e redirecionando a saída do sistema"
        def candidato = new Candidato("Diego", "diego@x.com", "444.444.444-44", 35, "RS", "90000-000", "Especialista em cloud", ["Docker", "Java"])
        def saida = new ByteArrayOutputStream()
        def original = System.out
        System.out = new PrintStream(saida)

        when:
        candidato.exibirDados()
        System.out = original
        println "O método exibirDados() foi chamado, saída do sistema restaurada."
        def texto = saida.toString()
        println "O texto que o teste capturou foi:\n${texto}"

        then:
        texto.contains("Diego")
        texto.contains("444.444.444-44")
        texto.contains("Docker, Java")
        println "O texto capturado contém o nome, CPF e competências válidos!"
    }
}
