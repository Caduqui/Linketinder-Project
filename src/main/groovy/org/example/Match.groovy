package org.example

class Match {
    Candidato candidato
    Empresa empresa
    Vaga vaga

    Match(candidato, empresa, vaga) {
        this.candidato = candidato
        this.empresa = empresa
        this.vaga = vaga
    }

    void exibirMatch() {
        println "\nCandidatos e Vagas que houveram matches:\n"
        println "Candidato: ${candidato.nome}"
        println "E-mail do candidato: ${candidato.email}"
        println "Vaga: ${vaga.nome}"
        println "Empresa: ${empresa.nome}"
        println "E-mail da empresa: ${empresa.email}"
    }
}
