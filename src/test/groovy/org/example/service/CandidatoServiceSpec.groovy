package org.example.service

import org.example.model.Candidato
import org.example.repositorio.CandidatoRepositorio
import spock.lang.Specification

import java.time.LocalDate

/**
 *
 * @author Guilherme Lima Conte
 */

class CandidatoServiceSpec extends Specification{

    CandidatoRepositorio candidatoRepositorio = Mock(CandidatoRepositorio)
    CandidatoService candidatoService = new CandidatoService(candidatoRepositorio)

    Candidato candidato() {
        return new Candidato("Ana", "Silva", LocalDate.of(2003, 4, 21), "ana@gmail.com", "111",
                "Brasil", "0", "d", "123456", "f", [])
    }

    def "cadastrar envia o candidato para o repositório"() {
        given:
        Candidato novo = candidato()

        when:
        candidatoService.cadastrar(novo)

        then:
        1 * candidatoRepositorio.inserir(novo)
    }

    def "atualizar mantém o ID do candidato escolhido"() {
        when:
        candidatoService.atualizar(7, candidato())

        then:
        1 * candidatoRepositorio.atualizar({Candidato atualizado -> atualizado.id == 7})
    }

    def "excluir repassa o ID para o repositório"() {
        when:
        candidatoService.excluir(3)

        then:
        1 * candidatoRepositorio.deletar(3)
    }
}
