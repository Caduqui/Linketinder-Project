package org.example

/**
 *
 * @author Guilherme Lima Conte
 */

class Empresa extends TipoPessoa {
    def cnpj
    def pais

    Empresa(def nome, def email, def cnpj, def pais, def estado, def cep, def descricao, List<String> competencias) {
        super (nome, email, cep, estado, descricao, competencias)
        this.cnpj = cnpj
        this.pais = pais
    }

    @Override
    void exibirDados() {
        println "\nEmpresa: ${nome}"
        println "email: ${email}"
        println "CNPJ: ${cnpj}"
        println "pais: ${pais}"
        println "Estado: ${estado}"
        println "CEP: ${cep}"
        println "Descrição: ${descricao}"
        println "Competencias: ${formatarCompetencias()}"
    }
}
