package org.example.Cadastro

import org.example.Candidato
import org.example.Empresa
import org.example.Match
import org.example.Vaga
import org.example.dao.CurtidaDAO
import org.example.dao.MatchDAO

class CadastroCurtida {

    final CurtidaDAO curtidaDAO
    final MatchDAO matchDAO
    final CadastroCandidato cadastroCandidato
    final CadastroEmpresa cadastroEmpresa
    final CadastroVaga cadastroVaga

    CadastroCurtida(cadastroCandidato, cadastroEmpresa, cadastroVaga, CurtidaDAO curtidaDAO = new CurtidaDAO(), MatchDAO matchDAO = new MatchDAO()) {
        this.cadastroCandidato = cadastroCandidato
        this.cadastroEmpresa = cadastroEmpresa
        this.cadastroVaga = cadastroVaga
        this.curtidaDAO = curtidaDAO
        this.matchDAO = matchDAO
    }


    boolean candidatoCurtirVaga() {
        cadastroCandidato.listarNomes()
        Candidato candidato = cadastroCandidato.selecionarCandidato("ID do candidato: ")
        if (candidato == null) {
            return false
        }

        println "\nVagas Disponíveis: "

        cadastroVaga.listar()
        Vaga vaga = cadastroVaga.selecionarVaga("ID da vaga que deseja curtir: ")

        if (vaga == null) {
            return false
        }

        if (!curtidaDAO.candidatoCurtirVaga(candidato.id, vaga.id)) {
            println "O candidato já curtiu essa vaga."
            return false
        }

        println "\nVaga curtida com sucesso"
        informarMatch(candidato.id, vaga.empresa.id, vaga.id)
        return true

    }

    boolean empresaCurtirCandidato() {
        cadastroEmpresa.listarNomes()
        Empresa empresa = cadastroEmpresa.selecionarEmpresa("ID da empresa: ")

        if (empresa == null) {
            return false
        }

        Vaga vaga = cadastroVaga.selecionarVagaDaEmpresa(empresa)
        if (vaga == null) {
            return false
        }

        println "\nCandidatos disponíveis: "
        cadastroCandidato.listarAnonimos()
        Candidato candidato = cadastroCandidato.selecionarCandidato("ID do candidato que deseja curtir: ")
        if (candidato == null) {
            return false
        }

        if (!curtidaDAO.empresaCurtirCandidato(empresa.id, candidato.id, vaga.id)) {
            println "A empresa já curtiu esse candidato para essa vaga."
            return false
        }

        println "\nCandidato curtido com sucesso para a vaga ${vaga.nome}!"
        informarMatch(candidato.id, empresa.id, vaga.id)
        return true
    }

    void listarMatches() {
        List<Match> matches = matchDAO.listar()

        if (matches.isEmpty()) {
            println "\nNenhum match ocorreu até o momento."
            return
        }

        println "\nCandidatos e vagas que deram match:"
        matches.each {
            it.exibirDados()
        }
    }

    void informarMatch(Integer idCandidato, Integer idEmpresa, Integer idVaga) {
        if (matchDAO.criarMatchSePossivel(idCandidato, idEmpresa, idVaga)) {
            println "\nMATCH!! A curtida entre vocês dois foi recíproca!!"
        } else {
            println "ainda não houve match."
        }
    }

}
