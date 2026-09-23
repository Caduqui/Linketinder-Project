package org.example.model

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

    String descrever() {
        return [
                "Candidato: ${candidato.nome} (${candidato.email})",
                "Vaga: ${vaga.nome}",
                "Empresa: ${empresa.nome} (${empresa.email})"
        ].join("\n")
    }
}
