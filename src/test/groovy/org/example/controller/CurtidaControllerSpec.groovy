package org.example.controller


import org.example.model.Candidato
import org.example.model.Empresa
import org.example.model.Vaga
import org.example.service.CurtidaService
import org.example.view.CurtidaView
import spock.lang.Specification

import java.time.LocalDate

/**
 *
 * @author Guilherme Lima Conte
 */

class CurtidaControllerSpec extends Specification {

    CurtidaService curtidaService = Mock(CurtidaService)
    CurtidaView curtidaView = Mock(CurtidaView)
    CandidatoController candidatoController = Mock(CandidatoController)
    EmpresaController empresaController = Mock(EmpresaController)
    VagaController vagaController = Mock(VagaController)

    CurtidaController curtidaController = new CurtidaController(curtidaService, curtidaView, candidatoController, empresaController, vagaController)

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
            "f",
            []
    )

    Empresa empresa = new Empresa(
            "TechVision",
            "t@x.com",
            "1", "Brasil",
            "0",
            "d",
            "123456"
    )

    Vaga vaga = new Vaga(
            "Dev Backend",
            "d",
            "MS",
            "Campo Grande",
            empresa,
            []
    )

    def setup() {
        candidato.id = 1
        empresa.id = 5
        vaga.id = 10
    }

    def "candidato curtir vaga registra a curtida e verifica o match"() {
        given:
        candidatoController.selecionar(_) >> candidato
        vagaController.selecionar(_) >> vaga
        curtidaService.candidatoCurtirVaga(1, 10) >> true

        when:
        boolean curtiu = curtidaController.candidatoCurtirVaga()

        then:
        1 * curtidaService.houveMatch(1, 5, 10) >> true
        1 * curtidaView.anunciarMatch()
        curtiu
    }

    def "curtida repetida do candidato não verifica match"() {
        given:
        candidatoController.selecionar(_) >> candidato
        vagaController.selecionar(_) >> vaga
        curtidaService.candidatoCurtirVaga(1, 10) >> false

        when:
        boolean curtiu = curtidaController.candidatoCurtirVaga()

        then:
        1 * curtidaView.avisarCurtidaRepetidaDoCandidato()
        0 * curtidaService.houveMatch(*_)
        !curtiu
    }

    def "empresa curtir candidato usa a vaga da própria empresa"() {
        given:
        empresaController.selecionar(_) >> empresa
        vagaController.selecionarVagaDaEmpresa(empresa) >> vaga
        candidatoController.selecionar(_) >> candidato

        when:
        boolean curtiu = curtidaController.empresaCurtirCandidato()

        then:
        1 * curtidaService.empresaCurtirCandidato(5, 1, 10) >> true
        1 * curtidaService.houveMatch(1, 5, 10) >> false
        1 * curtidaView.anunciarSemMatch()
        curtiu
    }

    def "empresa não curte ninguém quando a vaga não pertence a ela"() {
        given:
        empresaController.selecionar(_) >> empresa
        vagaController.selecionarVagaDaEmpresa(empresa) >> null

        when:
        boolean curtiu = curtidaController.empresaCurtirCandidato()

        then:
        0 * curtidaService.empresaCurtirCandidato(*_)
        !curtiu
    }

    def "listarMatches entrega à view o que o service devolve"() {
        given:
        curtidaService.listarMatches() >> []

        when:
        curtidaController.listarMatches()

        then:
        1 * curtidaView.exibirMatches([])
    }
}
