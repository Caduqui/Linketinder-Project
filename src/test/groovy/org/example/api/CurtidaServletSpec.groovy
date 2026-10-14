package org.example.api

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.example.model.Candidato
import org.example.model.Empresa
import org.example.model.Vaga
import org.example.service.CandidatoService
import org.example.service.EmpresaService
import org.example.service.CurtidaService
import org.example.service.VagaService
import org.example.service.CompetenciaService
import org.example.service.RecrutadorService
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.time.LocalDate

/**
 *
 * @author Guilherme Lima Conte
 */

class CurtidaServletSpec extends Specification {

    CandidatoService candidatoService = Mock(CandidatoService)
    VagaService vagaService = Mock(VagaService)
    CurtidaService curtidaService = Mock(CurtidaService)
    HttpServletRequest requisicao = Mock(HttpServletRequest)
    HttpServletResponse resposta = Mock(HttpServletResponse)
    StringWriter saida = new StringWriter()

    CurtidaServlet servlet = new CurtidaServlet()

    def setup() {
        servlet.dependencias = new Dependencias(candidatoService, Mock(EmpresaService), vagaService, Mock(CompetenciaService), curtidaService, Mock(RecrutadorService))
        resposta.writer >> new PrintWriter(saida)
        requisicao.method >> "POST"

        candidatoService.buscarPorId(9) >> candidato()
        vagaService.buscarPorId(3) >> vagaDaEmpresa(5)
    }

    void enviar(Map corpo) {
        requisicao.reader >> new BufferedReader(new StringReader(JsonOutput.toJson(corpo)))
    }

    Map respostaJson() {
        return (Map) new JsonSlurper().parseText(saida.toString())
    }

    Candidato candidato() {
        Candidato candidato = new Candidato("Fernando", "Silva", LocalDate.of(2000, 1, 15), "fernando@example.com", "12345678900", "Brasil", "79000000", "", "senha123", "", ["Java"])
        candidato.id = 9
        return candidato
    }

    Vaga vagaDaEmpresa(Integer idEmpresa) {
        Empresa empresa = new Empresa("CloudX", "rh@example.com", "12345678000100", "Brasil", "79000000", "", "senha123")
        empresa.id = idEmpresa

        Vaga vaga = new Vaga("Dev Java", "APIs", "MS", "Campo Grande", empresa, ["Java"])
        vaga.id = 3
        return vaga
    }

    def "candidato curte a vaga e responde 201 informando que ainda não houve match"() {
        given:
        enviar([tipo: "candidato", idCandidato: 9, idVaga: 3])

        when:
        servlet.service(requisicao, resposta)

        then:
        1 * curtidaService.candidatoCurtirVaga(9, 3)
        1 * curtidaService.houveMatch(9, 5, 3) >> false
        1 * resposta.setStatus(HttpServletResponse.SC_CREATED)
        respostaJson().match == false
    }

    def "empresa curte o candidato para a própria vaga e responde 201 com match"() {
        given:
        enviar([tipo: "empresa", idEmpresa: 5, idCandidato: 9, idVaga: 3])

        when:
        servlet.service(requisicao, resposta)

        then:
        1 * curtidaService.empresaCurtirCandidato(5, 9, 3)
        1 * curtidaService.houveMatch(9, 5, 3) >> true
        1 * resposta.setStatus(HttpServletResponse.SC_CREATED)
        respostaJson().match == true
    }

    def "empresa não pode curtir candidato por uma vaga de outra empresa"() {
        given:
        enviar([tipo: "empresa", idEmpresa: 8, idCandidato: 9, idVaga: 3])

        when:
        servlet.service(requisicao, resposta)

        then:
        0 * curtidaService.empresaCurtirCandidato(_, _, _)
        1 * resposta.setStatus(HttpServletResponse.SC_BAD_REQUEST)
        respostaJson().erro.contains("não pertence")
    }

    def "curtida de vaga inexistente responde 404 e não grava"() {
        given:
        enviar([tipo: "candidato", idCandidato: 9, idVaga: 77])

        when:
        servlet.service(requisicao, resposta)

        then:
        0 * curtidaService.candidatoCurtirVaga(_, _)
        1 * resposta.setStatus(HttpServletResponse.SC_NOT_FOUND)
    }

    def "curtida com tipo desconhecido responde 400"() {
        given:
        enviar([tipo: "admin", idCandidato: 9, idVaga: 3])

        when:
        servlet.service(requisicao, resposta)

        then:
        0 * curtidaService.candidatoCurtirVaga(_, _)
        0 * curtidaService.empresaCurtirCandidato(_, _, _)
        1 * resposta.setStatus(HttpServletResponse.SC_BAD_REQUEST)
    }
}
