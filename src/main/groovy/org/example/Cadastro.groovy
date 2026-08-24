package org.example

class Cadastro {

    final List<Candidato> candidatos
    final List<Empresa> empresas
    final List<Vaga> vagas
    final LeitorEntrada leitor

    Cadastro(List<Candidato> candidatos, List<Empresa> empresas, List<Vaga> vagas, LeitorEntrada leitor) {
        this.candidatos = candidatos
        this.empresas = empresas
        this.vagas = vagas
        this.leitor = leitor
    }

    Candidato cadastrarCandidato() {
        String nome = leitor.lerLinha("Nome: ")
        String sobrenome = leitor.lerLinha("Sobrenome: ")
        def dataNascimento = leitor.lerLinha("Data de nascimento: ")
        String email = leitor.lerLinha("Email: ")
        String cpf = leitor.lerLinha("CPF: ")
        String pais = leitor.lerLinha("País: ")
        String cep = leitor.lerLinha("CEP: ")
        String descricao = leitor.lerLinha("Descrição: ")
        String senha = leitor.lerLinha("Senha: ")
        while (senha.length() < 6) {
            println "A senha deve possuir no mínimo 6 caracteres."
            senha = leitor.lerLinha("Senha: ")
        }
        String formacao = leitor.lerLinha("Formação: ")
        List<String> competencias = leitor.lerLinha("Competências: ").split(",")*.trim()

        Candidato novoCandidato = new Candidato(nome, sobrenome, dataNascimento, email, cpf, pais, cep, descricao, senha, formacao, competencias)
        candidatos << novoCandidato
        return novoCandidato
    }

    Empresa cadastrarEmpresa() {
        String nome = leitor.lerLinha("Nome: ")
        String email = leitor.lerLinha("Email: ")
        String pais = leitor.lerLinha("País: ")
        String cep = leitor.lerLinha("CEP: ")
        String descricao = leitor.lerLinha("Descrição: ")
        String senha = leitor.lerLinha("Senha: ")
        while (senha.length() < 6) {
            println "A senha deve possuir no mínimo 6 caracteres."
            senha = leitor.lerLinha("Senha: ")
        }
        String cnpj = leitor.lerLinha("CNPJ: ")

        Empresa novaEmpresa = new Empresa(nome, email, cnpj, pais, cep, descricao, senha)
        empresas << novaEmpresa
        return novaEmpresa
    }

    Vaga cadastrarVaga(Empresa empresa) {
        String nome = leitor.lerLinha("Nome da vaga: ")
        String descricao = leitor.lerLinha("Descrição: ")
        String estado = leitor.lerLinha("Estado: ")
        String cidade = leitor.lerLinha("Cidade: ")
        List<String> competencias = leitor.lerLinha("Competências exigidas: ").split(",")*.trim()

        Vaga novaVaga = new Vaga(nome, descricao, estado, cidade, empresa, competencias)
        vagas << novaVaga
        return novaVaga
    }

}
