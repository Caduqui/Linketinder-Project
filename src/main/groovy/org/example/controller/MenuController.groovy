package org.example.controller

import org.example.service.RegraDeNegocioException
import org.example.view.MenuView

import java.sql.SQLException

/**
 *
 * @author Guilherme Lima Conte
 */

class MenuController {

    static final List<String> OPCOES_CRUD = ["Listar", "Cadastrar", "Atualizar", "Excluir"]

    private final MenuView menuView
    private final CandidatoController candidatoController
    private final EmpresaController empresaController
    private final CompetenciaController competenciaController
    private final VagaController vagaController
    private final CurtidaController curtidaController

    MenuController(MenuView menuView, CandidatoController candidatoController, EmpresaController empresaController, CompetenciaController competenciaController, VagaController vagaController, CurtidaController curtidaController) {
        this.menuView = menuView
        this.candidatoController = candidatoController
        this.empresaController = empresaController
        this.competenciaController = competenciaController
        this.vagaController = vagaController
        this.curtidaController = curtidaController
    }

    void executar() {
        while (true) {
            String opcao = menuView.lerOpcao("Bem vindo ao linketinder", ["Candidatos", "Empresas", "Competências", "Vagas", "Venha encontrar seu match"], "Sair")

            if (opcao == menuView.OPCAO_VOLTAR) {
                return
            }

            try {
                abrirMenu(opcao)
            } catch(RegraDeNegocioException e) {
                menuView.exibirMensagem(e.message)
            } catch (SQLException e) {
                menuView.exibirErroDeBanco(e.message)
            }
        }
    }

    private void abrirMenu(String opcao) {
        switch (opcao) {
            case "1":
                menuCrud("Candidatos", candidatoController)
                break
            case "2":
                menuCrud("Empresas", empresaController)
                break
            case "3":
                menuCrud("Competências", competenciaController)
                break
            case "4":
                menuCrud("Vagas", vagaController)
                break
            case "5":
                menuCurtidas()
                break
            default:
                menuView.avisarOpcaoInvalida()
        }
    }

    private void menuCrud(String titulo, ControllerCrud controller) {
        while (true) {
            String opcao = menuView.lerOpcao(titulo, OPCOES_CRUD, "Voltar")
            if (opcao == MenuView.OPCAO_VOLTAR) {
                return
            }

            try {
                executarOpcaoCrud(opcao, controller)
            } catch(RegraDeNegocioException e) {
                menuView.exibirMensagem(e.message)
            }
        }
    }

    private void executarOpcaoCrud(String opcao, ControllerCrud controller) {
        switch (opcao) {
            case "1":
                controller.listar()
                break
            case "2":
                controller.cadastrar()
                break
            case "3":
                controller.atualizar()
                break
            case "4":
                controller.excluir()
                break
            default:
                menuView.avisarOpcaoInvalida()
        }
    }

    private void menuCurtidas() {
        while (true) {
            String opcao = menuView.lerOpcao("Matches", ["Candidato curtir uma vaga", "Empresa curtir um candidato", "Listar matches"], "Voltar")

            if (opcao == MenuView.OPCAO_VOLTAR) {
                return
            }

            try {
                executarOpcaoCurtida(opcao)
            } catch (RegraDeNegocioException e) {
                menuView.exibirMensagem(e.message)
            }
        }
    }

    private void executarOpcaoCurtida(String opcao) {
        switch (opcao) {
            case "1":
                curtidaController.candidatoCurtirVaga()
                break
            case "2":
                curtidaController.empresaCurtirCandidato()
                break
            case "3":
                curtidaController.listarMatches()
                break
            default:
                menuView.avisarOpcaoInvalida()
        }
    }
}
