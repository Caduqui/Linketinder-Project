package org.example.Cadastro

import org.example.Candidato
import org.example.Empresa
import org.example.SaidaConsole
import org.example.Vaga
import org.example.dao.CurtidaDAO
import org.example.dao.MatchDAO
import spock.lang.Specification

import java.time.LocalDate

class CadastroCurtidaSpec extends Specification {

    CadastroCandidato cadastroCandidato = Mock(CadastroCandidato)
    CadastroEmpresa cadastroEmpresa = Mock(CadastroEmpresa)
    CadastroVaga cadastroVaga = Mock(CadastroVaga)
    CurtidaDAO curtidaDAO = Mock(CurtidaDAO)
    MatchDAO matchDAO = Mock(MatchDAO)

    CadastroCurtida cadastroCurtida = new CadastroCurtida(cadastroCandidato, cadastroEmpresa, cadastroVaga, curtidaDAO, matchDAO)

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
        cadastroCandidato.selecionarCandidato(_) >> candidato
        cadastroVaga.selecionarVaga(_) >> vaga
        curtidaDAO.candidatoCurtirVaga(1, 10) >> true

        when:
        String saida = SaidaConsole.capturar { assert cadastroCurtida.candidatoCurtirVaga() }

        then:
        1 * matchDAO.criarMatchSePossivel(1, 5, 10) >> true
        saida.contains("MATCH")
    }

    def "curtida repetida do candidato não verifica match"() {
        given:
        cadastroCandidato.selecionarCandidato(_) >> candidato
        cadastroVaga.selecionarVaga(_) >> vaga
        curtidaDAO.candidatoCurtirVaga(1, 10) >> false

        when:
        boolean curtiu = cadastroCurtida.candidatoCurtirVaga()

        then:
        0 * matchDAO.criarMatchSePossivel(*_)
        !curtiu
    }

    def "empresa curtir candidato usa a vaga da própria empresa"() {
        given:
        cadastroEmpresa.selecionarEmpresa(_) >> empresa
        cadastroVaga.selecionarVagaDaEmpresa(empresa) >> vaga
        cadastroCandidato.selecionarCandidato(_) >> candidato

        when:
        boolean curtiu = cadastroCurtida.empresaCurtirCandidato()

        then:
        1 * curtidaDAO.empresaCurtirCandidato(5, 1, 10) >> true
        1 * matchDAO.criarMatchSePossivel(1, 5, 10) >> false
        curtiu
    }

    def "empresa não curte ninguém quando a vaga não pertence a ela"() {
        given:
        cadastroEmpresa.selecionarEmpresa(_) >> empresa
        cadastroVaga.selecionarVagaDaEmpresa(empresa) >> null

        when:
        boolean curtiu = cadastroCurtida.empresaCurtirCandidato()

        then:
        0 * curtidaDAO.empresaCurtirCandidato(*_)
        !curtiu
    }

    def "listarMatches avisa quando ainda não há matches"() {
        given:
        matchDAO.listar() >> []

        when:
        String saida = SaidaConsole.capturar { cadastroCurtida.listarMatches() }

        then:
        saida.contains("Nenhum match")
    }
}
