package org.example

/**
 *
 * @author Guilherme Lima Conte
 */

class Competencia {

    Integer id
    String nome

    Competencia(String nome) {
        this.nome = nome
    }

    void exibirDados() {
        println "ID: ${id} | Competência: ${nome}"
    }
}