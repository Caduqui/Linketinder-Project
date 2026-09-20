package org.example.Cadastro

import org.example.Competencia
import org.example.dao.CompetenciaDAO

class CadastroCompetencia implements CadastroCrud{

    final CompetenciaDAO competenciaDAO
    final EntradaDados entrada

    CadastroCompetencia(EntradaDados entrada, CompetenciaDAO competenciaDAO = new CompetenciaDAO()) {
        this.entrada = entrada
        this.competenciaDAO = competenciaDAO
    }

    @Override
    void listar() {
        competenciaDAO.listar().each {
            it.exibirDados()
        }
    }

    @Override
    boolean cadastrar() {
        String nome = lerNomeDisponivel("Nome da competência: ")

        if (nome == null) {
            return false
        }

        competenciaDAO.inserir(new Competencia(nome))
        println "Competência cadastrada com sucesso!"
        return true
    }

    @Override
    boolean atualizar() {
        Competencia competencia = selecionarCompetencia("ID da competência: ")
        if (competencia == null) {
            return false
        }

        String novoNome = lerNomeDisponivel("Novo nome: ")
        if (novoNome == null) {
            return false
        }

        competencia.nome = novoNome
        competenciaDAO.atualizar(competencia)
        println "Competência atualizada com sucesso!"
        return true
    }

    @Override
    boolean excluir() {
        Competencia competencia = selecionarCompetencia("ID da competencia: ")
        if (competencia == null) {
            return false
        }

        if (competenciaDAO.estaVinculadoACandidatoOuVaga(competencia.id)) {
            println "Não é possível excluir a competência porque ela está relacionada a candidato(s) ou vaga(s)."
            return false
        }

        competenciaDAO.deletar(competencia.id)
        println "Competência excluída com sucesso!"
        return true
    }

    Competencia selecionarCompetencia(String mensagem) {
        Integer id = entrada.lerId(mensagem)
        if (id == null) {
            return null
        }

        Competencia competencia = competenciaDAO.buscarPorId(id)
        if (competencia == null) {
            println "Competência não encontrada."
        }

        return competencia
    }

    String lerNomeDisponivel(String mensagem) {
        String nome = entrada.lerTexto(mensagem)

        if (nome.isEmpty()) {
            println "O nome da competência não pode ser vazio."
            return null
        }

        if (competenciaDAO.buscarPorNome(nome) != null) {
            println "Essa competência já está cadastrada!"
            return null
        }

        return nome
    }
}
