package org.example.Cadastro

import org.example.Competencia
import org.example.dao.CompetenciaDAO
import spock.lang.Specification

class CadastroCompetenciaSpec extends Specification{

    CompetenciaDAO competenciaDAO = Mock(CompetenciaDAO)

    CadastroCompetencia cadastroCom(Map<String, String> respostas) {
        return new CadastroCompetencia(EntradaFalsa.comRespostas(respostas), competenciaDAO)
    }

    Competencia competenciaExistente(Integer id, String nome) {
        Competencia competencia = new Competencia(nome)
        competencia.id = id
        return competencia
    }

    def "cadastrar insere uma competência nova"() {
        given:
        CadastroCompetencia cadastro = cadastroCom(["Nome da competência: ": " Kotlin "])
        competenciaDAO.buscarPorNome("Kotlin") >> null

        when:
        boolean cadastrou = cadastro.cadastrar()

        then:
        1 * competenciaDAO.inserir({ Competencia competencia -> competencia.nome == "Kotlin" })
        cadastrou
    }

    def "cadastrar recusa competência já cadastrada"() {
        given:
        CadastroCompetencia cadastro = cadastroCom(["Nome da competência: ": "Java"])
        competenciaDAO.buscarPorNome("Java") >> competenciaExistente(1, "Java")

        when:
        boolean cadastrou = cadastro.cadastrar()

        then:
        0 * competenciaDAO.inserir(_)
        !cadastrou
    }

    def "cadastrar recusa nome vazio"() {
        given:
        CadastroCompetencia cadastro = cadastroCom(["Nome da competência: ": "   "])

        when:
        boolean cadastrou = cadastro.cadastrar()

        then:
        0 * competenciaDAO.inserir(_)
        !cadastrou
    }

    def "excluir não remove competência vinculada a candidato ou vaga"() {
        given:
        CadastroCompetencia cadastro = cadastroCom(["ID da competencia: ": "1"])
        competenciaDAO.buscarPorId(1) >> competenciaExistente(1, "Java")
        competenciaDAO.estaVinculadoACandidatoOuVaga(1) >> true

        when:
        boolean excluiu = cadastro.excluir()

        then:
        0 * competenciaDAO.deletar(_)
        !excluiu
    }

}
