package org.example.Cadastro

import org.example.Modelo.Empresa
import org.example.Modelo.Vaga
import org.example.repositorio.VagaRepositorio

/**
 *
 * @author Guilherme Lima Conte
 */

class CadastroVaga implements CadastroCrud{

    final VagaRepositorio vagaRepositorio
    final EntradaDados entrada
    final CadastroEmpresa cadastroEmpresa

    CadastroVaga(EntradaDados entrada, CadastroEmpresa cadastroEmpresa, VagaRepositorio vagaRepositorio) {
        this.entrada = entrada
        this.cadastroEmpresa = cadastroEmpresa
        this.vagaRepositorio = vagaRepositorio
    }

    @Override
    void listar() {
        vagaRepositorio.listar().each {
            it.exibirDados()
        }
    }

    @Override
    boolean cadastrar() {
        vagaRepositorio.inserir(lerDadosVaga())
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
        vagaRepositorio.atualizar(atualizada)
        println "Vaga atualizada com sucesso!"
        return true
    }

    @Override
    boolean excluir() {
        Vaga vaga = selecionarVaga("ID da vaga: ")
        if (vaga == null) {
            return false
        }

        vagaRepositorio.deletar(vaga.id)
        println "Vaga excluída com sucesso!"
        return true
    }

    Vaga selecionarVaga(String mensagem) {
        Integer id = entrada.lerId(mensagem)
        if (id == null) {
            return null
        }

        Vaga vaga = vagaRepositorio.buscarPorId(id)
        if (vaga == null) {
            println "Vaga não encontrada"
        }

        return vaga
    }

    Vaga selecionarVagaDaEmpresa(Empresa empresa) {
        List<Vaga> vagasDaEmpresa = vagaRepositorio.listarPorEmpresa(empresa.id)

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
