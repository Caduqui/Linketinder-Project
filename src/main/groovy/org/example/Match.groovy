package org.example

/**
 *
 * @author Guilherme Lima Conte
 */

class Match {
    Candidato candidato
    Empresa empresa
    Vaga vaga

    Match(Candidato candidato, Empresa empresa, Vaga vaga) {
        this.candidato = candidato
        this.empresa = empresa
        this.vaga = vaga
    }

    void exibirDados() {
        println "\nCandidato: ${candidato.nome} (${candidato.email})"
        println "Vaga: ${vaga.nome}"
        println "Empresa: ${empresa.nome} (${empresa.email})"
    }
}
