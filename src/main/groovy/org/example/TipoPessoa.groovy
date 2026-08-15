package org.example

/**
 *
 * @author Guilherme Lima Conte
 */

abstract class TipoPessoa implements Pessoa {
    def nome
    def email
    def cep
    def estado
    def descricao
    def competencias = []

    TipoPessoa(def nome, def email, def cep, def estado, def descricao, def competencias) {
        this.nome = nome
        this.email = email
        this.cep = cep
        this.estado = estado
        this.descricao = descricao
        this.competencias = competencias
    }

    String formatarCompetencias() {
        competencias ? competencias.join(", ") : "Nenhuma competência cadastrada"
    }
}
