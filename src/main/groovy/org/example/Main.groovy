package org.example

import org.example.dao.CandidatoDAO
import org.example.dao.CompetenciaDAO
import org.example.dao.EmpresaDAO
import org.example.dao.VagaDAO

/**
 *
 * @author Guilherme Lima Conte
 */

class Main {

    static CandidatoDAO candidatoDAO = new CandidatoDAO()
    static EmpresaDAO empresaDAO = new EmpresaDAO()
    static CompetenciaDAO competenciaDAO = new CompetenciaDAO()
    static VagaDAO vagaDAO = new VagaDAO()
    static Scanner scanner = new Scanner(System.in)
    static Cadastro cadastro

    static void main(String[] args) {
        cadastro = new Cadastro(new ScannerLeitorEntrada(scanner))
        menu()
    }

    static void menu() {
        while (true) {
            println "\n1- Candidatos"
            println "2- Empresas"
            println "3- Competências"
            println "4- Vagas"
            println "5- Venha encontrar seu match"
            println "0- Sair"
            print "Escolha sua opção: "

            switch (scanner.nextLine().trim()) {
                case "1":
                    menuCandidatos()
                    break
                case "2":
                    menuEmpresas()
                    break
                case "3":
                    menuCompetencias()
                    break
                case "4":
                    menuVagas()
                    break
                case "5":
                    menuCurtidas()
                    break
                case "0":
                    return
                default:
                    println "Opção inválida."
            }
        }
    }

    static void menuCandidatos() {
        while (true) {
            println "\nCANDIDATOS"
            println "1- Listar"
            println "2- Cadastrar"
            println "3- Atualizar"
            println "4- Excluir"
            println "0- Voltar"
            print "Escolha sua opção: "

            switch (scanner.nextLine().trim()) {
                case "1":
                    candidatoDAO.listar().each {
                        it.exibirDados()
                    }
                    break
                case "2":
                    cadastro.cadastrarCandidato()
                    println "Candidato cadastrado com sucesso!"
                    break
                case "3":
                    cadastro.atualizarCandidato()
                    println "Candidato atualizado com sucesso!"
                    break
                case "4":
                    if (cadastro.excluirCandidato()) {
                        println "Candidato excluído com sucesso!"
                    }

                    break
                case "0":
                    return
                default:
                    println "Opção inválida."
            }
        }
    }

    static void menuEmpresas() {
        while (true) {
            println "\nEMPRESAS"
            println "1- Listar"
            println "2- Cadastrar"
            println "3- Atualizar"
            println "4- Excluir"
            println "0- Voltar"
            print "Escolha sua opção: "

            switch (scanner.nextLine().trim()) {
                case "1":
                    empresaDAO.listar().each {
                        it.exibirDados()
                    }
                    break
                case "2":
                    cadastro.cadastrarEmpresa()
                    println "Empresa cadastrada com sucesso!"
                    break
                case "3":
                    cadastro.atualizarEmpresa()
                    println "Empresa atualizada com sucesso!"
                    break
                case "4":
                    cadastro.excluirEmpresa()
                    println "Empresa excluída com sucesso!"
                    break
                case "0":
                    return
                default:
                    println "Opção inválida."
            }
        }
    }

    static void menuCompetencias() {
        while (true) {
            println "\nCOMPETÊNCIAS"
            println "1- Listar"
            println "2- Cadastrar"
            println "3- Atualizar"
            println "4- Excluir"
            println "0- Voltar"
            print "Escolha sua opção: "

            switch (scanner.nextLine().trim()) {
                case "1":
                    competenciaDAO.listar().each {
                        it.exibirDados()
                    }
                    break
                case "2":
                    cadastro.cadastrarCompetencia()
                    println "Competência cadastrada com sucesso!"
                    break
                case "3":
                    cadastro.atualizarCompetencia()
                    println "Competência atualizada com sucesso!"
                    break
                case "4":
                    cadastro.excluirCompetencia()
                    println "Competência excluída com sucesso!"
                    break
                case "0":
                    return
                default:
                    println "Opção inválida."
            }
        }
    }

    static void menuVagas() {
        while (true) {
            println "\nVAGAS"
            println "1- Listar"
            println "2- Cadastrar"
            println "3- Atualizar"
            println "4- Excluir"
            println "0- Voltar"
            print "Escolha sua opção: "

            switch (scanner.nextLine().trim()) {
                case "1":
                    vagaDAO.listar().each {
                        it.exibirDados()
                    }
                    break
                case "2":
                    cadastro.cadastrarVaga()
                    println "Vaga cadastrada com sucesso!"
                    break
                case "3":
                    cadastro.atualizarVaga()
                    println "Vaga atualizada com sucesso!"
                    break
                case "4":
                    cadastro.excluirVaga()
                    println "Vaga excluída com sucesso!"
                    break
                case "0":
                    return
                default:
                    println "Opção inválida."
            }
        }
    }

    static void menuCurtidas() {
        while (true) {
            println "\nMATCHES"
            println "1- Candidato curtir uma vaga"
            println "2- Empresa curtir um candidato"
            println "3- Listar matches"
            println "0- Voltar"
            print "Escolha sua opção: "

            switch (scanner.nextLine().trim()) {
                case "1":
                    cadastro.candidatoCurtirVaga()
                    break
                case "2":
                    cadastro.empresaCurtirCandidato()
                    break
                case "3":
                    List<Match> matches = cadastro.listarMatches()

                    if (matches.isEmpty()) {
                        println "\nNenhum match ocorreu até o momento"
                    } else {
                        matches.each {
                            it.exibirMatch()
                        }
                    }
                    break
                case "0":
                    return
                default:
                    println "Opção inválida."
            }
        }
    }

}

