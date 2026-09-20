package org.example.Cadastro

import org.example.Empresa
import org.example.Vaga
import org.example.dao.VagaDAO

class CadastroVaga implements CadastroCrud{

    final VagaDAO vagaDAO = new VagaDAO()
    final EntradaDados entrada
    final CadastroEmpresa cadastroEmpresa

    CadastroVaga(EntradaDados entrada, CadastroEmpresa cadastroEmpresa, VagaDAO vagaDAO = new VagaDAO()) {
        this.entrada = entrada
        this.cadastroEmpresa = cadastroEmpresa
        this.vagaDAO = vagaDAO
    }

    @Override
    void listar() {
        vagaDAO.listar().each {
            it.exibirDados()
        }
    }

    @Override
    boolean cadastrar() {
        vagaDAO.inserir(lerDadosVaga())
        println "Vaga cadastrada com sucesso!"
        return true
    }

    @Override
    boolean atualizar() {
        Vaga vaga = selecionarVaga("ID da vaga: ")
        if (vaga == null) {
            return false
        }

        Vaga atualizada = lerDadosVaga()
        atualizada.id = vaga.id
        vagaDAO.atualizar(atualizada)
        println "Vaga atualizada com sucesso!"
        return true
    }

    @Override
    boolean excluir() {
        Vaga vaga = selecionarVaga("ID da vaga: ")
        if (vaga == null) {
            return false
        }

        vagaDAO.deletar(vaga.id)
        println "Vaga excluída com sucesso!"
        return true
    }

    Vaga selecionarVaga(String mensagem) {
        Integer id = entrada.lerId(mensagem)
        if (id == null) {
            return null
        }

        Vaga vaga = vagaDAO.buscarPorId(id)
        if (vaga == null) {
            println "Vaga não encontrada"
        }

        return vaga
    }

    Vaga selecionarVagaDaEmpresa(Empresa empresa) {
        List<Vaga> vagasDaEmpresa = vagaDAO.listarPorEmpresa(empresa.id)

        if (vagasDaEmpresa.isEmpty()) {
            println "Essa empresa não possui vagas cadastradas"
            return null
        }

        println "\nVagas da empresa ${empresa.nome}:"
        vagasDaEmpresa.each {
            it.exibirDados()
        }

        Integer idVaga = entrada.lerId("ID da vaga: ")
        Vaga vaga = vagasDaEmpresa.find {
            it.id == idVaga
        }

        if (vaga == null) {
            println "Essa vaga não pertence a esta empresa."
        }
        return vaga
    }

    Vaga lerDadosVaga() {
        Empresa empresa = selecionarEmpresaResponsavel()
        String nome = entrada.lerTexto("Nome da vaga: ")
        String descricao = entrada.lerTexto("Descrição: ")
        String estado = entrada.lerSiglaEstado()
        String cidade = entrada.lerTexto("Cidade: ")
        List<String> competencias = entrada.lerCompetencias("Competências exigidas: ")

        return new Vaga(nome, descricao, estado, cidade, empresa, competencias)
    }

    Empresa selecionarEmpresaResponsavel() {
        Empresa empresa = null
        while (empresa == null) {
            empresa = cadastroEmpresa.selecionarEmpresa("ID da empresa responsável pela vaga: ")
        }

        return empresa
    }
}
