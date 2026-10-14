package org.example.service

import org.example.model.Recrutador
import org.example.repositorio.RecrutadorRepositorio
import spock.lang.Specification

import java.time.LocalDate

/**
 *
 * @author Guilherme Lima Conte
 */
class RecrutadorServiceSpec extends Specification {

    RecrutadorRepositorio recrutadorRepositorio = Mock(RecrutadorRepositorio)
    RecrutadorService recrutadorService = new RecrutadorService(recrutadorRepositorio)

    Recrutador criarRecrutador() {
        return new Recrutador(
                "Ana Paula",
                "TI",
                "Líder de tecnologia",
                "ana.paula@example.com",
                "120.120.120-12",
                LocalDate.of(1990, 7, 12),
                "123456",
                1
        )
    }

    def "cadastra recrutador válido"() {
        given:
        Recrutador recrutador = criarRecrutador()
        recrutadorRepositorio.buscarPorEmail(_ as String) >> null
        recrutadorRepositorio.buscarPorCpf(_ as String) >> null

        when:
        recrutadorService.cadastrar(recrutador)

        then:
        1 * recrutadorRepositorio.inserir(recrutador)
    }

    def "recusa cadastro com e-mail ou CPF repetido"() {
        given:
        Recrutador recrutador = criarRecrutador()
        recrutadorRepositorio.buscarPorEmail(recrutador.email) >> new Recrutador("Ana", "RH", "", "ana.paula@example.com", "555.555.555-55", LocalDate.of(1988, 1, 1), "123456", 1)

        when:
        recrutadorService.cadastrar(recrutador)

        then:
        RegraDeNegocioException erro = thrown()
        erro.message == "Já existe um recrutador com esses dados"
        0 * recrutadorRepositorio.inserir(_)
    }

    def "recusa cadastro com CPF repetido"() {
        given:
        Recrutador recrutador = criarRecrutador()
        Recrutador existente = new Recrutador("Bruno", "RH", "", "bruno@example.com", recrutador.cpf, LocalDate.of(1988, 1, 1), "123456", 1)
        existente.id = 2
        recrutadorRepositorio.buscarPorEmail(recrutador.email) >> null
        recrutadorRepositorio.buscarPorCpf(recrutador.cpf) >> existente

        when:
        recrutadorService.cadastrar(recrutador)

        then:
        RegraDeNegocioException erro = thrown()
        erro.message == "Já existe um recrutador com esses dados"
        0 * recrutadorRepositorio.inserir(_)
    }

    def "atualiza dados mantendo o próprio e-mail e CPF"() {
        given:
        Recrutador existente = criarRecrutador()
        existente.id = 1
        Recrutador dados = criarRecrutador()
        recrutadorRepositorio.buscarPorEmail(dados.email) >> existente
        recrutadorRepositorio.buscarPorCpf(dados.cpf) >> existente

        when:
        recrutadorService.atualizar(1, dados)

        then:
        1 * recrutadorRepositorio.atualizar(dados)
    }

    def "recusa atualização com e-mail de outro recrutador"() {
        given:
        Recrutador dados = criarRecrutador()
        Recrutador existente = new Recrutador("Bruno", "RH", "", dados.email, "555.555.555-55", LocalDate.of(1988, 1, 1), "123456", 1)
        existente.id = 2
        recrutadorRepositorio.buscarPorEmail(dados.email) >> existente
        recrutadorRepositorio.buscarPorCpf(dados.cpf) >> null

        when:
        recrutadorService.atualizar(1, dados)

        then:
        RegraDeNegocioException erro = thrown()
        erro.message == "Já existe um recrutador com esses dados"
        0 * recrutadorRepositorio.atualizar(_)
    }

    def "autentica recrutador com e-mail e senha corretos"() {
        given:
        Recrutador recrutador = criarRecrutador()
        recrutadorRepositorio.buscarPorEmail("ana.paula@example.com") >> recrutador

        expect:
        recrutadorService.autenticar("ana.paula@example.com", "123456") == recrutador
        recrutadorService.autenticar("ana.paula@example.com", "senha-errada") == null
    }
}
