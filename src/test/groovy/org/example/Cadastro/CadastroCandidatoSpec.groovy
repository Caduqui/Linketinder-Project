package org.example.Cadastro

import org.example.Modelo.Candidato
import org.example.repositorio.CandidatoRepositorio
import spock.lang.Specification

import java.time.LocalDate

/**
 *
 * @author Guilherme Lima Conte
 */

class CadastroCandidatoSpec extends Specification{

    static final Map<String, String> DADOS_CANDIDATO = [
            "Nome: " : "Fernanda",
            "Sobrenome: " : "Costa",
            "Data de nascimento (AAAA-MM-DD): " : "1999-05-10",
            "Email: " : "fernanda@email.com",
            "CPF: " : "999.999.999-99",
            "País: " : "Brasil",
            "CEP: " : "88010-000",
            "Descrição: " : "Entusiasta de cloud",
            "Senha: " : "senha123",
            "Formação: " : "Computação",
            "Competências: " : "Java, Docker"
    ]

    CandidatoRepositorio candidatoRepositorio = Mock(CandidatoRepositorio)

    CadastroCandidato cadastroCom(Map<String, String> respostas) {
        return new CadastroCandidato(EntradaFalsa.comRespostas(respostas), candidatoRepositorio)
    }

    Candidato candidatoExistente(Integer id) {
        Candidato candidato = new Candidato(
                "Ana",
                "Silva",
                LocalDate.of(2003, 4, 21),
                "ana@gmail.com",
                "111",
                "Brasil",
                "0",
                "d",
                "123456",
                "f", [])
        candidato.id = id
        return candidato
    }

    def "cadastrar lê os dados digitados e insere o cnadidato"() {
        given:
        CadastroCandidato cadastro = cadastroCom(DADOS_CANDIDATO)

        when:
        boolean cadastrou = cadastro.cadastrar()

        then:
        1 * candidatoRepositorio.inserir({ Candidato candidato ->
            candidato.nome == "Fernanda" &&
                    candidato.sobrenome == "Costa" &&
                    candidato.dataNascimento == LocalDate.of(1999, 5, 10) &&
                    candidato.cpf == "999.999.999-99" &&
                    candidato.competencias == ["Java", "Docker"]
        })
        cadastrou
    }

    def "atualizar mantém o ID do candidato escolhido"() {
        given:
        CadastroCandidato cadastro = cadastroCom(DADOS_CANDIDATO + ["ID do candidato: ": "1"])
        candidatoRepositorio.buscarPorId(1) >> candidatoExistente(1)

        when:
        boolean atualizou = cadastro.atualizar()

        then:
        1 * candidatoRepositorio.atualizar({ Candidato candidato ->
            candidato.id == 1 && candidato.nome == "Fernanda"
        })
        atualizou
    }

    def "atualizar não altera nada quando o candidato não existe"() {
        given:
        CadastroCandidato cadastro = cadastroCom(["ID do candidato: ": "99"])
        candidatoRepositorio.buscarPorId(99) >> null

        when:
        boolean atualizou = cadastro.atualizar()

        then:
        0 * candidatoRepositorio.atualizar(_)
        !atualizou
    }

    def "excluir remove o candidato existente"() {
        given:
        CadastroCandidato cadastro = cadastroCom(["ID do candidato: ": "1"])
        candidatoRepositorio.buscarPorId(1) >> candidatoExistente(1)

        when:
        boolean excluiu = cadastro.excluir()

        then:
        1 * candidatoRepositorio.deletar(1)
        excluiu
    }

    def "excluir não faz nada quando o ID digitado é inválido"() {
        given:
        CadastroCandidato cadastro = cadastroCom(["ID do candidato: ": "alguma coisa"])

        when:
        boolean excluiu = cadastro.excluir()

        then:
        0 * candidatoRepositorio.buscarPorId(_)
        0 * candidatoRepositorio.deletar(_)
        !excluiu
    }

}
