package org.example.Cadastro

import org.example.Empresa
import org.example.repositorio.EmpresaRepositorio
import spock.lang.Specification

/**
 *
 * @author Guilherme Lima Conte
 */

class CadastroEmpresaSpec extends Specification{

    EmpresaRepositorio empresaRepositorio = Mock(EmpresaRepositorio)

    CadastroEmpresa cadastroCom(Map<String, String> respostas) {
        return new CadastroEmpresa(EntradaFalsa.comRespostas(respostas), empresaRepositorio)
    }

    Empresa empresaExistente(Integer id) {
        Empresa empresa = new Empresa(
                "CloudX",
                "contato@cloudx.com",
                "99",
                "Brasil",
                "0",
                "d",
                "123456")
        empresa.id = id
        return empresa
    }

    def "cadastrar lê os dados digitados e insere a empresa"() {
        given:
        CadastroEmpresa cadastro = cadastroCom([
                "Nome: " : "CloudX",
                "Email: " : "contato@cloudx.com",
                "País: " : "Brasil",
                "CEP: " : "01001-000",
                "Descrição: " : "SaaS de gestão",
                "CNPJ: " : "99.999.999/0001-99",
                "Senha: " : "senha123"
        ])

        when:
        boolean cadastrou = cadastro.cadastrar()

        then:
        1 * empresaRepositorio.inserir({ Empresa empresa -> empresa.nome == "CloudX" && empresa.cnpj == "99.999.999/0001-99" })
        cadastrou
    }

    def "excluir não remove empresa que possui vagas"() {
        given:
        CadastroEmpresa cadastro = cadastroCom(["ID da empresa: ": "1"])
        empresaRepositorio.buscarPorId(1) >> empresaExistente(1)
        empresaRepositorio.possuiVagas(1) >> true

        when:
        boolean excluiu = cadastro.excluir()

        then:
        0 * empresaRepositorio.deletar(_)
        !excluiu
    }

    def "excluir remove empresa sem vagas"() {
        given:
        CadastroEmpresa cadastro = cadastroCom(["ID da empresa: ": "1"])
        empresaRepositorio.buscarPorId(1) >> empresaExistente(1)
        empresaRepositorio.possuiVagas(1) >> false

        when:
        boolean excluiu = cadastro.excluir()

        then:
        1 * empresaRepositorio.deletar(1)
        excluiu
    }

    def "selecionarEmpresa retorna null quando a empresa não existe"() {
        given:
        CadastroEmpresa cadastro = cadastroCom(["ID: ": "42"])
        empresaRepositorio.buscarPorId(42) >> null

        expect:
        cadastro.selecionarEmpresa("ID: ") == null
    }
}
