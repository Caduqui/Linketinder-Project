package org.example.Cadastro

import org.example.Modelo.Competencia
import org.example.repositorio.CompetenciaRepositorio

/**
 *
 * @author Guilherme Lima Conte
 */

class CadastroCompetencia implements CadastroCrud{

    final CompetenciaRepositorio competenciaRepositorio
    final EntradaDados entrada

    CadastroCompetencia(EntradaDados entrada, CompetenciaRepositorio competenciaRepositorio) {
        this.entrada = entrada
        this.competenciaRepositorio = competenciaRepositorio
    }

    @Override
    void listar() {
        competenciaRepositorio.listar().each {
            it.exibirDados()
        }
    }

    @Override
    boolean cadastrar() {
        String nome = lerNomeDisponivel("Nome da competência: ")

        if (nome == null) {
            return false
        }

        competenciaRepositorio.inserir(new Competencia(nome))
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
        competenciaRepositorio.atualizar(competencia)
        println "Competência atualizada com sucesso!"
        return true
    }

    @Override
    boolean excluir() {
        Competencia competencia = selecionarCompetencia("ID da competencia: ")
        if (competencia == null) {
            return false
        }

        if (competenciaRepositorio.estaVinculadoACandidatoOuVaga(competencia.id)) {
            println "Não é possível excluir a competência porque ela está relacionada a candidato(s) ou vaga(s)."
            return false
        }

        competenciaRepositorio.deletar(competencia.id)
        println "Competência excluída com sucesso!"
        return true
    }

    Competencia selecionarCompetencia(String mensagem) {
        Integer id = entrada.lerId(mensagem)
        if (id == null) {
            return null
        }

        Competencia competencia = competenciaRepositorio.buscarPorId(id)
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

        if (competenciaRepositorio.buscarPorNome(nome) != null) {
            println "Essa competência já está cadastrada!"
            return null
        }

        return nome
    }
}
