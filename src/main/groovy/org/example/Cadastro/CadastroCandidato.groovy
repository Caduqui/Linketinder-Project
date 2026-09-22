package org.example.Cadastro

import org.example.Modelo.Candidato
import org.example.repositorio.CandidatoRepositorio

import java.time.LocalDate

/**
 *
 * @author Guilherme Lima Conte
 */

class CadastroCandidato implements CadastroCrud{

    final CandidatoRepositorio candidatoRepositorio
    final EntradaDados entrada

    CadastroCandidato(EntradaDados entrada, CandidatoRepositorio candidatoRepositorio) {
        this.entrada = entrada
        this.candidatoRepositorio = candidatoRepositorio
    }

    @Override
    void listar() {
        candidatoRepositorio.listar().each {
            it.exibirDados()
        }
    }

    void listarNomes() {
        candidatoRepositorio.listar().each { candidato ->
            println "ID: ${candidato.id} - ${candidato.nome} ${candidato.sobrenome}"
        }
    }

    void listarAnonimos() {
        candidatoRepositorio.listar().each {
            it.exibirDadosAnonimos()
        }
    }

    @Override
    boolean cadastrar() {
        candidatoRepositorio.inserir(lerDadosCandidato())
        println "Candidato cadastrado com sucesso!"
        return true
    }

    @Override
    boolean atualizar() {
        Candidato candidato = selecionarCandidato("ID do candidato: ")
        if (candidato == null) {
            return false
        }

        Candidato atualizado = lerDadosCandidato()
        atualizado.id = candidato.id
        candidatoRepositorio.atualizar(atualizado)
        println "Candidato atualizado com sucesso!"
        return true
    }

    @Override
    boolean excluir() {
        Candidato candidato = selecionarCandidato("ID do candidato: ")
        if (candidato == null) {
            return false
        }

        candidatoRepositorio.deletar(candidato.id)
        println "Candidato excluído com sucesso!"
        return true
    }

    Candidato selecionarCandidato(String mensagem) {
        Integer id = entrada.lerId(mensagem)
        if (id == null) {
            return null
        }

        Candidato candidato = candidatoRepositorio.buscarPorId(id)
        if (candidato == null) {
            println "Candidato não encontrado"
        }
        return candidato
    }

    Candidato lerDadosCandidato() {
        String nome = entrada.lerTexto("Nome: ")
        String sobrenome = entrada.lerTexto("Sobrenome: ")
        LocalDate dataNascimento = entrada.lerDataNascimento()
        String email = entrada.lerTexto("Email: ")
        String cpf = entrada.lerTexto("CPF: ")
        String pais = entrada.lerTexto("País: ")
        String cep = entrada.lerTexto("CEP: ")
        String descricao = entrada.lerTexto("Descrição: ")
        String senha = entrada.lerSenha()
        String formacao = entrada.lerTexto("Formação: ")
        List<String> competencias = entrada.lerCompetencias("Competências: ")

        return new Candidato(nome, sobrenome, dataNascimento, email, cpf, pais, cep, descricao, senha, formacao, competencias)
    }

}
