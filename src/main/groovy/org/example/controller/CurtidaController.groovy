package org.example.controller

import org.example.model.Candidato
import org.example.model.Empresa
import org.example.model.Vaga
import org.example.service.CurtidaService
import org.example.view.CurtidaView

class CurtidaController {

    private final CurtidaService curtidaService
    private final CurtidaView curtidaView
    private final CandidatoController candidatoController
    private final EmpresaController empresaController
    private final VagaController vagaController

    CurtidaController(CurtidaService curtidaService, CurtidaView curtidaView, CandidatoController candidatoController, EmpresaController empresaController, VagaController vagaController) {
        this.curtidaService = curtidaService
        this.curtidaView = curtidaView
        this.candidatoController = candidatoController
        this.empresaController = empresaController
        this.vagaController = vagaController
    }


    boolean candidatoCurtirVaga() {
        candidatoController.listarNomes()
        Candidato candidato = candidatoController.selecionar("ID do candidato: ")
        if (candidato == null) {
            return false
        }

        curtidaView.exibirVagasDisponiveis()
        vagaController.listar()
        Vaga vaga = vagaController.selecionar("ID da vaga que deseja curtir: ")
        if (vaga == null) {
            return false
        }

        if (!curtidaService.candidatoCurtirVaga(candidato.id, vaga.id)) {
            curtidaView.avisarCurtidaRepetidaDoCandidato()
            return false
        }

        curtidaView.avisarVagaCurtida()
        informarMatch(candidato.id, vaga.empresa.id, vaga.id)
        return true
    }

    boolean empresaCurtirCandidato() {
        empresaController.listarNomes()
        Empresa empresa = empresaController.selecionar("ID da empresa: ")

        if (empresa == null) {
            return false
        }

        Vaga vaga = vagaController.selecionarVagaDaEmpresa(empresa)
        if (vaga == null) {
            return false
        }

        candidatoController.listarAnonimos()
        Candidato candidato = candidatoController.selecionar("ID do candidato que deseja curtir: ")
        if (candidato == null) {
            return false
        }

        if (!curtidaService.empresaCurtirCandidato(empresa.id, candidato.id, vaga.id)) {
            curtidaView.avisarCurtidaRepetidaDaEmpresa()
            return false
        }

        curtidaView.avisarCandidatoCurtido(vaga)
        informarMatch(candidato.id, empresa.id, vaga.id)
        return true
    }

    void listarMatches() {
        curtidaView.exibirMatches(curtidaService.listarMatches())
    }

    private void informarMatch(Integer idCandidato, Integer idEmpresa, Integer idVaga) {
        if (curtidaService.houveMatch(idCandidato, idEmpresa, idVaga)) {
            curtidaView.anunciarMatch()
        } else {
            curtidaView.anunciarSemMatch()
        }
    }
}
