package org.example.Modelo

/**
 *
 * @author Guilherme Lima Conte
 */

class Empresa extends TipoPessoa {
    Integer id
    String cnpj

    Empresa(String nome, String email, String cnpj, String pais, String cep, String descricao, String senha) {
        super (nome, email, pais, cep, descricao, senha)
        this.cnpj = cnpj
    }

    @Override
    void exibirDados() {
        println "\nID: ${id} | Empresa: ${nome}"
        println "email: ${email}"
        println "CNPJ: ${cnpj}"
        println "País: ${pais}"
        println "CEP: ${cep}"
        println "Descrição: ${descricao}"
    }
}
