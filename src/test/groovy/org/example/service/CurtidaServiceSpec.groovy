package org.example.service

import org.example.repositorio.CurtidaRepositorio
import org.example.repositorio.MatchRepositorio
import spock.lang.Specification

/**
 *
 * @author Guilherme Lima Conte
 */

class CurtidaServiceSpec extends Specification{

    CurtidaRepositorio curtidaRepositorio = Mock(CurtidaRepositorio)
    MatchRepositorio matchRepositorio = Mock(MatchRepositorio)
    CurtidaService curtidaService = new CurtidaService(curtidaRepositorio, matchRepositorio)

    def "candidatoCurtirVaga informa quando a curtida é nova"() {
        given:
        curtidaRepositorio.candidatoCurtirVaga(1, 10) >> true

        expect:
        curtidaService.candidatoCurtirVaga(1, 10)
    }

    def "candidatoCurtirVaga informa quando a curtida é repetida"() {
        given:
        curtidaRepositorio.candidatoCurtirVaga(1, 10) >> false

        expect:
        !curtidaService.candidatoCurtirVaga(1, 10)
    }

    def "houveMatch consulta o repositório de matches"() {
        given:
        matchRepositorio.criarMatchSePossivel(1, 5, 10) >> true

        expect:
        curtidaService.houveMatch(1, 5, 10)
    }

    def "listarMatches devolve o que o repositório tem"() {
        given:
        matchRepositorio.listar() >> []

        expect:
        curtidaService.listarMatches().isEmpty()
    }
}
