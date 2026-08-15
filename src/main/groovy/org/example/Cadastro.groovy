package org.example

class Cadastro {

    final List<Candidato> candidatos
    final List<Empresa> empresas
    final LeitorEntrada leitor

    Cadastro(List<Candidato> candidatos, List<Empresa> empresas, LeitorEntrada leitor) {
        this.candidatos = candidatos
        this.empresas = empresas
        this.leitor = leitor
    }

    Candidato cadastrarCandidato() {
        def nome = leitor.lerLinha("Nome: ")
        def email = leitor.lerLinha("Email: ")
        def cpf = leitor.lerLinha("CPF: ")
        def idade = leitor.lerLinha("Idade: ").toInteger()
        def estado = leitor.lerLinha("Estado: ")
        def cep = leitor.lerLinha("CEP: ")
        def descricao = leitor.lerLinha("Descrição: ")
        List<String> competencias = leitor.lerLinha("Competências: ").split(",")*.trim()

        Candidato novoCandidato = new Candidato(nome, email, cpf, idade, estado, cep, descricao, competencias)
        candidatos << novoCandidato
        return novoCandidato
    }

    Empresa cadastrarEmpresa() {
        def nome = leitor.lerLinha("Nome: ")
        def email = leitor.lerLinha("Email: ")
        def cnpj = leitor.lerLinha("CNPJ: ")
        def pais = leitor.lerLinha("País: ")
        def estado = leitor.lerLinha("Estado: ")
        def cep = leitor.lerLinha("CEP: ")
        def descricao = leitor.lerLinha("Descrição: ")
        List<String> competencias = leitor.lerLinha("Competências: ").split(",")*.trim()

        Empresa novaEmpresa = new Empresa(nome, email, cnpj, pais, estado, cep, descricao, competencias)
        empresas << novaEmpresa
        return novaEmpresa
    }
}
