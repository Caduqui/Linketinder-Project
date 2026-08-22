package org.example

import spock.lang.Specification

class CadastroSpec extends Specification{

    List<Candidato> candidatos
    List<Empresa> empresas

    def setup() {
        candidatos = []
        empresas = []
        println "Listas de candidatos e empresas inicializado"
    }

    def "cadastrarCandidato lê os dados pelo LeitorEntrada e insere na lista"() {
        given:
        def leitor = Mock(LeitorEntrada)
        def operacao = new Cadastro (candidatos, empresas, leitor)

        when:
        println "Chamando operacao cadastrarCandidato()"
        def novoCandidato = operacao.cadastrarCandidato()

        then:
        1 * leitor.lerLinha("Nome: ") >> "Fernanda"
        1 * leitor.lerLinha("Email: ") >> "fernanda@email.com"
        1 * leitor.lerLinha("CPF: ") >> "999.999.999-99"
        1 * leitor.lerLinha("Idade: ") >> "26"
        1 * leitor.lerLinha("Estado: ") >> "SC"
        1 * leitor.lerLinha("CEP: ") >> "88010-000"
        1 * leitor.lerLinha("Descrição: ") >> "Entusiasta de cloud"
        1 * leitor.lerLinha("Competências: ") >> "Java, Docker"

        and: "o candidato foi inserido na lista com os dados corretos"
        candidatos.size() == 1
        candidatos[0].is(novoCandidato)
        novoCandidato.nome == "Fernanda"
        novoCandidato.idade == 26
        novoCandidato.competencias == ["Java", "Docker"]

        println "Candidato '${novoCandidato.nome}' validado e inserido na lista de candidatos."

        and: "não afetou a lista de empresas"
        empresas.isEmpty()
        println "A lista de empresas permaneceu vazia."
    }

    def "cadastrarEmpresa lẽ os dados pelo LeitorEntrada e insere na lista"() {
        given:
        def leitor = Mock(LeitorEntrada)
        def operacao = new Cadastro(candidatos, empresas, leitor)

        when:
        println "Chamando operacao cadastrarEmpresa()"
        def novaEmpresa = operacao.cadastrarEmpresa()

        then:
        1 * leitor.lerLinha("Nome: ") >> "CloudX"
        1 * leitor.lerLinha("Email: ") >> "contato@cloudx.com"
        1 * leitor.lerLinha("CNPJ: ") >> "99.999.999/0001-99"
        1 * leitor.lerLinha("País: ") >> "Brasil"
        1 * leitor.lerLinha("Estado: ") >> "SP"
        1 * leitor.lerLinha("CEP: ") >> "01001-000"
        1 * leitor.lerLinha("Descrição: ") >> "SaaS de gestão"
        1 * leitor.lerLinha("Competências: ") >> "Java,Docker"

        and: "a empresa foi inserida na lista com os dados corretos"
        empresas.size() == 1
        empresas[0].is(novaEmpresa)
        novaEmpresa.nome == "CloudX"
        novaEmpresa.competencias == ["Java", "Docker"]

        println "Empresa '${novaEmpresa.nome}' validada e inserida na lista de empresas."

        and: "não afetou a lista de candidatos"
        candidatos.isEmpty()
        println "A lista de candidatos permaneceu vazia."
    }

    def "cadastrar mais de um candidato não acontece sobreescrevimento"() {
        given:
        def leitor = Stub(LeitorEntrada)
        leitor.lerLinha(_) >>> [
                "Ana", "ana@x.com", "111", "20", "SP", "01000-000", "desc1", "Java",
                "Bruno", "bruno@x.com", "222", "25", "RJ", "02000-000", "desc2", "Python"
        ]
        def operacao = new Cadastro(candidatos, empresas, leitor)

        when:
        println "Cadastrando dois candidatos em sequência"
        operacao.cadastrarCandidato()
        operacao.cadastrarCandidato()

        then:
        candidatos.size() == 2
        candidatos*.nome == ["Ana", "Bruno"]
        println "A lista contém ${candidatos.size()} candidatos: ${candidatos*.nome}."
    }

    def "cadastrar candidatos e empresas junto não faz um array interferir no outro"() {
        given:
        def leitorCandidato = Stub(LeitorEntrada)
        leitorCandidato.lerLinha(_) >>> ["Ana", "ana@x.com", "111", "20", "SP", "01000-000", "desc", "Java"]

        def leitorEmpresa = Stub(LeitorEntrada)
        leitorEmpresa.lerLinha(_) >>> ["Cloud", "c@x.com", "99", "Brasil", "SP", "0", "desc", "Java"]

        when:
        println "-> Cadastrando um candidato e uma empresa"
        new Cadastro(candidatos, empresas, leitorCandidato).cadastrarCandidato()
        new Cadastro(candidatos, empresas, leitorEmpresa).cadastrarEmpresa()

        then:
        candidatos.size() == 1
        empresas.size() == 1
        println "1 Candidato e 1 Empresa cadastrados em suas respectivas listas."
    }
}
