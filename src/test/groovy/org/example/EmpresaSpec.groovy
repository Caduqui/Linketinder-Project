package org.example

import spock.lang.Specification
import spock.lang.Unroll

class EmpresaSpec extends Specification {

    def "O construtor preenche corretamente todos os campos"() {
        given:
        def competencias = ["Java", "React"]
        println "Competências definidas como ${competencias}."

        when:
        println "Criando uma nova instância de Empresa"
        def empresa = new Empresa("Technova", "contato@technova.com", "11.111.111/0001-11", "Brasil", "SP", "01001-000", "Empresa de tecnologia", competencias)

        then:
        empresa.nome == "Technova"
        empresa.email == "contato@technova.com"
        empresa.cnpj == "11.111.111/0001-11"
        empresa.pais == "Brasil"
        empresa.estado == "SP"
        empresa.cep == "01001-000"
        empresa.descricao == "Empresa de tecnologia"
        empresa.competencias == competencias
        println "Todos os atributos do construtor foram preenchidos corretamente na classe Empresa."
    }

    def "Empresa é uma instância de Pessoa e TipoPessoa"() {
        expect:
        println "Checando se Empresa implementa a interface Pessoa e estende TipoPessoa"
        new Empresa("X", "exemplo@gmail.com", "1", "Brasil", "MS", "0", "d", []) instanceof Pessoa
        new Empresa("X", "exemplo@gmail.com", "1", "Brasil", "MS", "0", "d", []) instanceof TipoPessoa
        println "A classe Empresa herda corretamente"
    }

    @Unroll
    def "formatarCompetencias('#competencias') retorna '#esperado'"() {
        given:
        def empresa = new Empresa("Loja", "l@x.com", "22", "Brasil", "RJ", "20000", "desc", competencias)
        println "Testando formatação para as competências de entrada: ${competencias}"

        expect:
        empresa.formatarCompetencias() == esperado
        println "O método processou e retornou a string exata: '${esperado}'."

        where:
        competencias | esperado
        ["SQL", "Java"] | "SQL, Java"
        ["Scrum"] | "Scrum"
        [] | "Nenhuma competência cadastrada"
    }

    def "exibirDados imprime as informações da empresa no console"() {
        given:
        println "Criando empresa e redirecionando a saída do sistema"
        def empresa = new Empresa("DataWise", "jobs@datawise.com", "44.444.444/0001-44", "Brasil", "MG", "30130-000", "Consultoria de dados", ["Python", "SQL"])
        def saida = new ByteArrayOutputStream()
        def original = System.out
        System.out = new PrintStream(saida)

        when:
        empresa.exibirDados()
        System.out = original
        println "O método exibirDados() foi chamado, saída do sistema restaurada."
        def texto = saida.toString()
        println "O texto que o teste capturou foi:\n${texto}"

        then:
        texto.contains("DataWise")
        texto.contains("44.444.444/0001-44")
        texto.contains("Python, SQL")
        println "O texto capturado contém o nome, CNPJ e competências válidos."
    }


}
