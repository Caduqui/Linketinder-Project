package org.example

import org.example.dao.CandidatoDAO
import org.example.dao.CompetenciaDAO
import org.example.dao.CurtidaDAO
import org.example.dao.EmpresaDAO
import org.example.dao.MatchDAO
import org.example.dao.VagaDAO

/**
 *
 * @author Guilherme Lima Conte
 */

class Cadastro {

    final CandidatoDAO candidatoDAO = new CandidatoDAO()
    final EmpresaDAO empresaDAO = new EmpresaDAO()
    final VagaDAO vagaDAO = new VagaDAO()
    final CompetenciaDAO competenciaDAO = new CompetenciaDAO()
    final CurtidaDAO curtidaDAO = new CurtidaDAO()
    final MatchDAO matchDAO = new MatchDAO()
    final LeitorEntrada leitor

    Cadastro(LeitorEntrada leitor) {
        this.leitor = leitor
    }

    Integer lerId(String mensagem) {
        try {
            return Integer.parseInt(leitor.lerLinha(mensagem).trim())
        } catch (NumberFormatException e) {
            println("ID inválido")
            return null
        }
    }

    String lerSenha() {
        String senha = leitor.lerLinha("Senha: ")
        while (senha.length() < 6) {
            println "A senha deve possuir no mínimo 6 caracteres"
            senha = leitor.lerLinha("Senha: ")
        }
        return senha
    }

    List<String> lerCompetencias(String mensagem) {
        return leitor.lerLinha(mensagem).split(",")*.trim().findAll { !it.isEmpty() }
    }

    Candidato lerDadosCandidato() {
        String nome = leitor.lerLinha("Nome: ")
        String sobrenome = leitor.lerLinha("Sobrenome: ")
        def dataNascimento = leitor.lerLinha("Data de nascimento: ")
        String email = leitor.lerLinha("Email: ")
        String cpf = leitor.lerLinha("CPF: ")
        String pais = leitor.lerLinha("País: ")
        String cep = leitor.lerLinha("CEP: ")
        String descricao = leitor.lerLinha("Descrição: ")
        String senha = lerSenha()
        String formacao = leitor.lerLinha("Formação: ")
        List<String> competencias = lerCompetencias("Competências: ")

        return new Candidato(nome, sobrenome, dataNascimento, email, cpf, pais, cep, descricao, senha, formacao, competencias)
    }

    Candidato cadastrarCandidato() {
        Candidato novoCandidato = lerDadosCandidato()
        candidatoDAO.inserir(novoCandidato)
        return novoCandidato
    }

    boolean atualizarCandidato() {
        Integer id = lerId("ID do candidato: ")
        if (id == null) {
            return false
        }

        Candidato candidato = candidatoDAO.buscarPorId(id)
        if (candidato == null) {
            println "Candidato não encontrado"
            return false
        }

        Candidato atualizado = lerDadosCandidato()
        atualizado.id = id
        candidatoDAO.atualizar(atualizado)
        return true
    }

    boolean excluirCandidato() {
        Integer id = lerId("ID do candidato: ")
        if (id == null) {
            return false
        }

        if (candidatoDAO.buscarPorId(id) == null) {
            println "Candidato não encontrado."
            return false
        }

        candidatoDAO.deletar(id)
        return true
    }

    Empresa lerDadosEmpresa() {
        String nome = leitor.lerLinha("Nome: ")
        String email = leitor.lerLinha("Email: ")
        String pais = leitor.lerLinha("País: ")
        String cep = leitor.lerLinha("CEP: ")
        String descricao = leitor.lerLinha("Descrição: ")
        String senha = lerSenha()
        String cnpj = leitor.lerLinha("CNPJ: ")

        return new Empresa(nome, email, cnpj, pais, cep, descricao, senha)
    }

    Empresa cadastrarEmpresa() {
        Empresa novaEmpresa = lerDadosEmpresa()
        empresaDAO.inserir(novaEmpresa)
        return novaEmpresa
    }

    boolean atualizarEmpresa() {
        Integer id = lerId("ID da empresa: ")
        if (id == null) {
            return false
        }

        Empresa empresa = empresaDAO.buscarPorId(id)
        if (empresa == null) {
            println "Empresa não encontrada."
            return false
        }

        Empresa atualizada = lerDadosEmpresa()
        atualizada.id = id

        empresaDAO.atualizar(atualizada)
        return true
    }

    boolean excluirEmpresa() {
        Integer id = lerId("ID da empresa: ")
        if (id == null) {
            return false
        }

        if (empresaDAO.buscarPorId(id) == null) {
            println "Empresa não encontrada."
            return false
        }

        if (empresaDAO.possuiVagas(id)) {
            println "Não é possível excluir a empresa porque ela possui vagas cadastradas."
            return false
        }

        empresaDAO.deletar(id)
        return true
    }

    Competencia cadastrarCompetencia() {
        String nome = leitor.lerLinha("Nome da competência: ").trim()

        Competencia existente = competenciaDAO.buscarPorNome(nome)
        if (existente != null) {
            println "Essa competência já está cadastrada"
            return null
        }

        Competencia competencia = new Competencia(nome)
        competenciaDAO.inserir(competencia)
        return competencia
    }

    boolean atualizarCompetencia() {
        Integer id = lerId("ID da competência: ")
        if (id == null) {
            return false
        }

        Competencia competencia = competenciaDAO.buscarPorId(id)
        if (competencia == null) {
            println "Competência não encontrada."
            return false
        }

        String novoNome = leitor.lerLinha("Novo nome: ").trim()
        competencia.nome = novoNome
        competenciaDAO.atualizar(competencia)
        return true
    }

    boolean excluirCompetencia() {
        Integer id = lerId("ID da competência: ")
        if (id == null) {
            return false
        }

        if (competenciaDAO.buscarPorId(id) == null) {
            println "Competência não encontrada."
            return false
        }

        if (competenciaDAO.estaEmUso(id)) {
            println "Não é possível excluir a competência porque ela está relacionada a candidato(s) ou vaga(s)."
            return false
        }

        competenciaDAO.deletar(id)
        return true
    }

    Empresa selecionarEmpresa() {
        while (true) {
            Integer idEmpresa = lerId("Id da empresa responsável pela vaga: ")

            if (idEmpresa == null) {
                continue
            }

            Empresa empresa = empresaDAO.buscarPorId(idEmpresa)

            if (empresa != null) {
                return empresa
            }

            println "Empresa não encontrada"
        }
    }

    Vaga lerDadosVaga(Empresa empresa) {
        String nome = leitor.lerLinha("Nome da vaga: ")
        String descricao = leitor.lerLinha("Descrição: ")
        String estado = leitor.lerLinha("Estado: ")
        String cidade = leitor.lerLinha("Cidade: ")
        List<String> competencias = leitor.lerLinha("Competências exigidas: ").split(",")*.trim()

        return new Vaga(nome, descricao, estado, cidade, empresa, competencias)
    }

    Vaga cadastrarVaga() {
        Empresa empresa = selecionarEmpresa()
        Vaga novaVaga = lerDadosVaga(empresa)
        vagaDAO.inserir(novaVaga)
        return novaVaga
    }

    boolean atualizarVaga() {
        Integer id = lerId("ID da vaga: ")
        if (id == null) {
            return false
        }

        Vaga vaga = vagaDAO.buscarPorId(id)
        if (vaga == null) {
            println "Vaga não encontrada."
            return false
        }

        Empresa empresa = selecionarEmpresa()
        Vaga atualizada = lerDadosVaga(empresa)
        atualizada.id = id
        vagaDAO.atualizar(atualizada)
        return true
    }

    boolean excluirVaga() {
        Integer id = lerId("ID da vaga: ")
        if (id == null) return false

        if (vagaDAO.buscarPorId(id) == null) {
            println "Vaga não encontrada."
            return false
        }

        vagaDAO.deletar(id)
        return true
    }

    boolean candidatoCurtirVaga() {
        candidatoDAO.listar().each { candidato ->
            println "ID: ${candidato.id} - ${candidato.nome} ${candidato.sobrenome}"
        }

        Integer idCandidato = lerId("ID do candidato: ")
        if (idCandidato == null) {
            return false
        }

        Candidato candidato = candidatoDAO.buscarPorId(idCandidato)
        if (candidato == null) {
            println "Candidato não encontrado"
            return false
        }

        println "\nVagas Disponíveis: "

        vagaDAO.listar().each { vaga ->
            println "\n\nID: ${vaga.id} - Vaga: ${vaga.nome}"
            println "Descrição: ${vaga.descricao}"
            println "Local: ${vaga.cidade} - ${vaga.estado}"
            println "Competências: ${vaga.formatarCompetencias()}"
        }

        Integer idVaga = lerId("Digite o ID da vaga que deseja curtir: ")
        if (idVaga == null) {
            return false
        }

        Vaga vaga = vagaDAO.buscarPorId(idVaga)
        if (vaga == null) {
            println "Vaga não encontrada"
            return false
        }

        boolean curtiu = curtidaDAO.candidatoCurtirVaga(candidato.id, vaga.id)

        if (!curtiu) {
            println "O candidato já curtiu essa vaga."
            return false
        }

        println "\nVaga curtida com sucesso"

        boolean houveMatch = matchDAO.criarMatchSePossivel(candidato.id, vaga.empresa.id, vaga.id)

        if (houveMatch) {
            println "\nMATCH! A empresa já havia curtido você para essa vaga."
        } else {
            println "Ainda não houve match."
        }

        return true

    }

    boolean empresaCurtirCandidato() {
        empresaDAO.listar().each { empresa ->
            println "ID: ${empresa.id} - ${empresa.nome}"
        }

        Integer idEmpresa = lerId("ID da empresa: ")

        if (idEmpresa == null) {
            return false
        }

        Empresa empresa = empresaDAO.buscarPorId(idEmpresa)

        if (empresa == null) {
            println "Empresa não encontrada"
            return false
        }

        List<Vaga> vagasDaEmpresa = vagaDAO.listarPorEmpresa(empresa.id)

        if (vagasDaEmpresa.isEmpty()) {
            println "Essa empresa não possui vagas cadastradas."
            return false
        }

        println "\nVagas da empresa ${empresa.nome}:"

        vagasDaEmpresa.each {
            println "\nID: ${it.id} - ${it.nome}"
            println "Descrição: ${it.descricao}"
            println "Local: ${it.cidade} - ${it.estado}"
            println "Competências: ${it.formatarCompetencias()}"
        }

        Integer idVaga = lerId("Digite o ID da vaga: ")

        if (idVaga == null) {
            return false
        }

        Vaga vaga = vagasDaEmpresa.find {
            it.id == idVaga
        }

        if (vaga == null) {
            println "Essa vaga não pertence a esta empresa"
            return false
        }

        println "\nCandidatos disponíveis:"
        candidatoDAO.listar().each { candidato ->
            println "\nID: ${candidato.id}"
            println "Formação: ${candidato.formacao}"
            println "Descrição: ${candidato.descricao}"
            println "Competências: ${candidato.formatarCompetencias()}"
        }

        Integer idCandidato = lerId("ID do candidato que deseja curtir: ")

        if (idCandidato == null) {
            return false
        }

        Candidato candidato = candidatoDAO.buscarPorId(idCandidato)

        if (candidato == null) {
            println "Candidato não encontrado"
            return false
        }

        boolean curtiu = curtidaDAO.empresaCurtirCandidato(idEmpresa, idCandidato, idVaga)

        if (!curtiu) {
            println "A empresa já curtiu esse candidato para essa vaga"
            return false
        }

        println "\nCandidato curtido com sucesso para a vaga ${vaga.nome}!"

        boolean houveMatch = matchDAO.criarMatchSePossivel(candidato.id, empresa.id, vaga.id)

        if (houveMatch) {
            println "\nMATCH! O candidato já havia curtido essa vaga."
        } else {
            println "Ainda não houve match."
        }

        return true
    }

    List<Match> listarMatches() {
        return matchDAO.listar()
    }
}
