package org.example

import org.example.Cadastro.CadastroCandidato
import org.example.Cadastro.CadastroCompetencia
import org.example.Cadastro.CadastroCrud
import org.example.Cadastro.CadastroCurtida
import org.example.Cadastro.CadastroEmpresa
import org.example.Cadastro.CadastroVaga
import org.example.Cadastro.EntradaDados
import org.example.dao.CandidatoDAO
import org.example.dao.CompetenciaDAO
import org.example.dao.CurtidaDAO
import org.example.dao.EmpresaDAO
import org.example.dao.MatchDAO
import org.example.dao.VagaDAO
import org.example.repositorio.CandidatoRepositorio
import org.example.repositorio.CompetenciaRepositorio
import org.example.repositorio.CurtidaRepositorio
import org.example.repositorio.EmpresaRepositorio
import org.example.repositorio.MatchRepositorio
import org.example.repositorio.VagaRepositorio

import java.sql.SQLException

/**
 *
 * @author Guilherme Lima Conte
 */

class Main {

    static final String OPCAO_VOLTAR = "0"
    static final List<String> OPCOES_CRUD = ["Listar", "Cadastrar", "Atualizar", "Excluir"]

    static final Scanner scanner = new Scanner(System.in)
    static final EntradaDados entrada = new EntradaDados(new ScannerLeitorEntrada(scanner))

    static final ConexaoBanco conexaoBanco = new ConexaoBanco()
    static final CompetenciaRepositorio competenciaRepositorio = new CompetenciaDAO(conexaoBanco)
    static final CandidatoRepositorio candidatoRepositorio = new CandidatoDAO(conexaoBanco, competenciaRepositorio)
    static final EmpresaRepositorio empresaRepositorio = new EmpresaDAO(conexaoBanco)
    static final VagaRepositorio vagaRepositorio = new VagaDAO(conexaoBanco, competenciaRepositorio, empresaRepositorio)
    static final CurtidaRepositorio curtidaRepositorio = new CurtidaDAO(conexaoBanco)
    static final MatchRepositorio matchRepositorio = new MatchDAO(conexaoBanco, candidatoRepositorio, empresaRepositorio, vagaRepositorio)

    static final CadastroCandidato cadastroCandidato = new CadastroCandidato(entrada, candidatoRepositorio)
    static final CadastroEmpresa cadastroEmpresa = new CadastroEmpresa(entrada, empresaRepositorio)
    static final CadastroCompetencia cadastroCompetencia = new CadastroCompetencia(entrada, competenciaRepositorio)
    static final CadastroVaga cadastroVaga = new CadastroVaga(entrada, cadastroEmpresa, vagaRepositorio)
    static final CadastroCurtida cadastroCurtida = new CadastroCurtida(cadastroCandidato, cadastroEmpresa, cadastroVaga, curtidaRepositorio, matchRepositorio)

    static void main(String[] args) {
        while (true) {
            exibirOpcoes("Bem vindo ao linketinder", ["Candidatos", "Empresas", "Competências", "Vagas", "Venha encontrar seu match"], "Sair")
            String opcao = lerOpcao()
            if (opcao == OPCAO_VOLTAR) {
                return
            }

            try {
                abrirMenu(opcao)
            } catch (SQLException e) {
                println "Erro ao acessar o banco de dados: ${e.message}"
            }
        }
    }

    static void abrirMenu(String opcao) {
        switch (opcao) {
            case "1":
                menuCrud("Candidatos", cadastroCandidato)
                break
            case "2":
                menuCrud("Empresas", cadastroEmpresa)
                break
            case "3":
                menuCrud("Competencias", cadastroCompetencia)
                break
            case "4":
                menuCrud("Vagas", cadastroVaga)
                break
            case "5":
                menuCurtidas()
                break
            default:
                println "Opção inválida."
        }
    }

    static void menuCrud(String titulo, CadastroCrud cadastro) {
        while (true) {
            exibirOpcoes(titulo, OPCOES_CRUD, "Voltar")

            switch (lerOpcao()) {
                case "1":
                    cadastro.listar()
                    break
                case "2":
                    cadastro.cadastrar()
                    break
                case "3":
                    cadastro.atualizar()
                    break
                case "4":
                    cadastro.excluir()
                    break
                case OPCAO_VOLTAR:
                    return
                default:
                    println "Opção inválida"
            }
        }
    }

    static void menuCurtidas() {
        while (true) {
            exibirOpcoes("Matches", ["Candidato curtir uma vaga", "Empresa curtir um candidato", "Listar matches"], "Voltar")

            switch (lerOpcao()) {
                case "1":
                    cadastroCurtida.candidatoCurtirVaga()
                    break
                case "2":
                    cadastroCurtida.empresaCurtirCandidato()
                    break
                case "3":
                    cadastroCurtida.listarMatches()
                    break
                case OPCAO_VOLTAR:
                    return
                default:
                    println "Opção inválida."
            }
        }
    }

    static void exibirOpcoes(String titulo, List<String> opcoes, String textoVoltar) {
        println "\n${titulo}"
        opcoes.eachWithIndex{ String opcao, int indice ->
            println "${indice + 1}- ${opcao}"
        }
        println "${OPCAO_VOLTAR}- ${textoVoltar}"
        print "Escolha sua opção: "
    }

    static String lerOpcao() {
        return scanner.nextLine().trim()
    }
}
