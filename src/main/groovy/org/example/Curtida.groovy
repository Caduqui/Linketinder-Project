package org.example

class Curtida {

    Candidato candidato
    Vaga vaga
    Empresa empresa

    Curtida(Candidato candidato, Vaga vaga) {
        this.candidato = candidato
        this.vaga = vaga
    }

    Curtida(Empresa empresa, Candidato candidato, Vaga vaga) {
        this.empresa = empresa
        this.candidato = candidato
        this.vaga = vaga
    }

    boolean ehCurtidaDoCandidato() {
        return candidato != null && vaga != null && empresa == null
    }

    boolean ehCurtidaDaEmpresa() {
        return empresa != null && vaga != null && candidato != null
    }
}
