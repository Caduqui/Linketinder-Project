package org.example.Cadastro

import org.example.Empresa
import org.example.Vaga
import org.example.dao.VagaDAO
import spock.lang.Specification

class CadastroVagaSpec extends Specification{

    VagaDAO vagaDAO = Mock(VagaDAO)
    CadastroEmpresa cadastroEmpresa = Mock(CadastroEmpresa)

    CadastroVaga cadastroCom(Map<String, String> respostas) {
        return new CadastroVaga(EntradaFalsa.comRespostas(respostas), cadastroEmpresa, vagaDAO)
    }

    Empresa empresa(Integer id) {
        Empresa empresa = new Empresa(
                "Empresa ${id}",
                "e@x.com",
                "1",
                "Brasil",
                "0",
                "d",
                "123456"
        )
        empresa.id = id
        return empresa
    }

    Vaga vaga(Integer id, Empresa empresa) {
        Vaga vaga = new Vaga(
                "Vaga ${id}",
                "d",
                "MT",
                "Cuiabá",
                empresa,
                []
        )
        vaga.id = id
        return vaga
    }

    def "cadastrar pede a empresa até encontrar uma válida e insere a vaga"() {
        given:
        CadastroVaga cadastro = cadastroCom([
                "Nome da vaga: " : "Dev Backend",
                "Descrição: " : "APIs",
                "Estado(sigla): " : "mt",
                "Cidade: " : "Cuiabá",
                "Competências exigidas: " : "Groovy, SQL"
        ])

        when:
        boolean cadastrou = cadastro.cadastrar()

        then:
        2 * cadastroEmpresa.selecionarEmpresa(_) >>> [null, empresa(1)]
        1 * vagaDAO.inserir({ Vaga vaga ->
            vaga.empresa.id == 1 && vaga.estado == "MT" && vaga.competencias == ["Groovy", "SQL"]
        })
        cadastrou
    }

    def "selecionarVagaDaEmpresa recusa vaga de outra empresa"() {
        given:
        Empresa empresaAtual = empresa(1)
        CadastroVaga cadastro = cadastroCom(["ID da vaga: ": "20"])
        vagaDAO.listarPorEmpresa(1) >> [vaga(10, empresaAtual)]

        expect:
        cadastro.selecionarVagaDaEmpresa(empresaAtual) == null
    }

    def "selecionarVagaDaEmpresa retorna a vaga escolhida quando ela pertence à empresa"() {
        given:
        Empresa empresaAtual = empresa(1)
        CadastroVaga cadastro = cadastroCom(["ID da vaga: ": "10"])
        vagaDAO.listarPorEmpresa(1) >> [vaga(10, empresaAtual)]

        expect:
        cadastro.selecionarVagaDaEmpresa(empresaAtual).id == 10
    }

    def "selecionarVagaDaEmpresa retorna null quando a empresa não tem vagas"() {
        given:
        CadastroVaga cadastro = cadastroCom([:])
        vagaDAO.listarPorEmpresa(1) >> []

        expect:
        cadastro.selecionarVagaDaEmpresa(empresa(1)) == null
    }
}
