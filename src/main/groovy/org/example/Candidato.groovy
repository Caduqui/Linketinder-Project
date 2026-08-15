package org.example

/**
 *
 * @author Guilherme Lima Conte
 */

class Candidato extends TipoPessoa{
    def cpf
    def idade

    Candidato (def nome, def email, def cpf, def idade, def estado, def cep, def descricao, List<String> competencias) {
        super(nome, email, cep, estado, descricao, competencias)
        this.cpf = cpf
        this.idade = idade
    }

    @Override
    void exibirDados() {
        println "\nCandidato: ${nome}"
        println "email: ${email}"
        println "CPF: ${cpf}"
        println "Idade: ${idade}"
        println "Estado: ${estado}"
        println "CEP: ${cep}"
        println "Descrição: ${descricao}"
        println "Competencias: ${formatarCompetencias()}"
    }
}
