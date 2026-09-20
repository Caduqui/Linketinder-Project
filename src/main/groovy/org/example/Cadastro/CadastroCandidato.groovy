package org.example.Cadastro

import org.example.Candidato
import org.example.dao.CandidatoDAO

import java.time.LocalDate

class CadastroCandidato implements CadastroCrud{

    final CandidatoDAO candidatoDAO
    final EntradaDados entrada

    CadastroCandidato(EntradaDados entrada, CandidatoDAO candidatoDAO = new CandidatoDAO()) {
        this.entrada = entrada
        this.candidatoDAO = candidatoDAO
    }

    @Override
    void listar() {
        candidatoDAO.listar().each {
            it.exibirDados()
        }
    }

    void listarNomes() {
        candidatoDAO.listar().each { candidato ->
            println "ID: ${candidato.id} - ${candidato.nome} ${candidato.sobrenome}"
        }
    }

    void listarAnonimos() {
        candidatoDAO.listar().each {
            it.exibirDadosAnonimos()
        }
    }

    @Override
    boolean cadastrar() {
        candidatoDAO.inserir(lerDadosCandidato())
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
        candidatoDAO.atualizar(atualizado)
        println "Candidato atualizado com sucesso!"
        return true
    }

    @Override
    boolean excluir() {
        Candidato candidato = selecionarCandidato("ID do candidato: ")
        if (candidato == null) {
            return false
        }

        candidatoDAO.deletar(candidato.id)
        println "Candidato excluído com sucesso!"
        return true
    }

    Candidato selecionarCandidato(String mensagem) {
        Integer id = entrada.lerId(mensagem)
        if (id == null) {
            return null
        }

        Candidato candidato = candidatoDAO.buscarPorId(id)
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
