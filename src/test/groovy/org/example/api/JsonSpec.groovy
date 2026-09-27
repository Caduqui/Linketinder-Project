package org.example.api

import org.example.model.Candidato
import org.example.model.Empresa
import org.example.model.Vaga
import org.example.service.RegraDeNegocioException
import spock.lang.Specification

import java.time.LocalDate

/**
 *
 * @author Guilherme Lima Conte
 */

class JsonSpec extends Specification{

    static final Map DADOS_CANDIDATO = [
            nome: "Cleiton", sobrenome: "Costa", dataNascimento: "1995-02-20",
            email: "cleiton@gmail.com", cpf: "999.999.999-99", pais: "Brasil", cep: "79091246",
            descricao: "Dev", senha: "senha123", formacao: "Computação",
            competencias: ["Java", "java", "  ", "Typescript"]
    ]

    def "paraCandidato converte o candidato e normaliza as competências"() {
        when:
        Candidato candidato = Json.paraCandidato(DADOS_CANDIDATO)

        then:
        candidato.nome == "Cleiton"
        candidato.dataNascimento == LocalDate.of(1995,2,20)
        candidato.competencias == ["Java", "Typescript"]
    }

    def "paraCandidato recusa requisição sem um campo obrigatório"() {
        when:
        Json.paraCandidato(DADOS_CANDIDATO - [nome: DADOS_CANDIDATO.nome])

        then:
        RegraDeNegocioException erro = thrown()
        erro.message.contains("'nome'")
    }

    def "paraCandidato recusa data fora do formato AAAA-MM-DD"() {
        when:
        Json.paraCandidato(DADOS_CANDIDATO + [dataNascimento: "20/02/1995"])

        then:
        RegraDeNegocioException erro = thrown()
        erro.message.contains("AAAA-MM-DD")
    }

    def "deCandidato não expõe a senha"() {
        given:
        Candidato candidato = Json.paraCandidato(DADOS_CANDIDATO)

        when:
        Map json = Json.deCandidato(candidato)

        then:
        json.nome == "Cleiton"
        json.dataNascimento == "1995-02-20"
        !json.containsKey("senha")
    }

    def "deEmpresa não expõe a senha"() {
        given:
        Empresa empresa = new Empresa("CloudX", "cloudx@gmail.com", "998889889", "Brasil", "7909123", "SaaS", "senha123")

        expect:
        !Json.deEmpresa(empresa).containsKey("senha")
    }

    def "deVaga identifica a empresa dona da vaga"() {
        given:
        Empresa empresa = new Empresa("CloudX", "cloudx@gmail.com", "998889889", "Brasil", "7909123", "SaaS", "senha123")
        empresa.id = 5
        Vaga vaga = new Vaga("Dev", "APIs", "MS", "Campo Grande", empresa, ["Groovy"])

        when:
        Map json = Json.deVaga(vaga)

        then:
        json.empresa == [id: 5, nome: "CloudX"]
        json.competencias == ["Groovy"]
    }

    def "exigirInteiro recusa valor que não é número"() {
        when:
        Json.exigirInteiro([idEmpresa: "abc"], "idEmpresa")

        then:
        RegraDeNegocioException erro = thrown()
        erro.message.contains("idEmpresa")
    }
}
