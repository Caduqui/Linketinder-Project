package org.example.Cadastro

import org.example.Competencia
import org.example.repositorio.CompetenciaRepositorio
import spock.lang.Specification

/**
 *
 * @author Guilherme Lima Conte
 */

class CadastroCompetenciaSpec extends Specification{

    CompetenciaRepositorio competenciaRepositorio = Mock(CompetenciaRepositorio)

    CadastroCompetencia cadastroCom(Map<String, String> respostas) {
        return new CadastroCompetencia(EntradaFalsa.comRespostas(respostas), competenciaRepositorio)
    }

    Competencia competenciaExistente(Integer id, String nome) {
        Competencia competencia = new Competencia(nome)
        competencia.id = id
        return competencia
    }

    def "cadastrar insere uma competência nova"() {
        given:
        CadastroCompetencia cadastro = cadastroCom(["Nome da competência: ": " Kotlin "])
        competenciaRepositorio.buscarPorNome("Kotlin") >> null

        when:
        boolean cadastrou = cadastro.cadastrar()

        then:
        1 * competenciaRepositorio.inserir({ Competencia competencia -> competencia.nome == "Kotlin" })
        cadastrou
    }

    def "cadastrar recusa competência já cadastrada"() {
        given:
        CadastroCompetencia cadastro = cadastroCom(["Nome da competência: ": "Java"])
        competenciaRepositorio.buscarPorNome("Java") >> competenciaExistente(1, "Java")

        when:
        boolean cadastrou = cadastro.cadastrar()

        then:
        0 * competenciaRepositorio.inserir(_)
        !cadastrou
    }

    def "cadastrar recusa nome vazio"() {
        given:
        CadastroCompetencia cadastro = cadastroCom(["Nome da competência: ": "   "])

        when:
        boolean cadastrou = cadastro.cadastrar()

        then:
        0 * competenciaRepositorio.inserir(_)
        !cadastrou
    }

    def "excluir não remove competência vinculada a candidato ou vaga"() {
        given:
        CadastroCompetencia cadastro = cadastroCom(["ID da competencia: ": "1"])
        competenciaRepositorio.buscarPorId(1) >> competenciaExistente(1, "Java")
        competenciaRepositorio.estaVinculadoACandidatoOuVaga(1) >> true

        when:
        boolean excluiu = cadastro.excluir()

        then:
        0 * competenciaRepositorio.deletar(_)
        !excluiu
    }

}
