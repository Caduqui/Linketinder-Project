package org.example

/**
 *
 * @author Guilherme Lima Conte
 */

class Empresa extends TipoPessoa {
    String cnpj

    Empresa(String nome, String email, String cnpj, String pais, String cep, String descricao, String senha) {
        super (nome, email, pais, cep, descricao, senha)
        this.cnpj = cnpj
    }

    @Override
    void exibirDados() {
        println "\nEmpresa: ${nome}"
        println "email: ${email}"
        println "CNPJ: ${cnpj}"
        println "pais: ${pais}"
        println "CEP: ${cep}"
        println "Descrição: ${descricao}"
    }
}
