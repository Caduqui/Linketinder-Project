package org.example

/**
 *
 * @author Guilherme Lima Conte
 */

class Candidato extends TipoPessoa{
    Integer id
    String sobrenome
    def dataNascimento
    String cpf
    String formacao
    List<String> competencias = []

    Candidato (String nome, String sobrenome, def dataNascimento,String email, String cpf, String pais, String cep, String descricao, String senha, String formacao, List<String> competencias) {
        super(nome, email, pais, cep, descricao, senha)
        this.sobrenome = sobrenome
        this.cpf = cpf
        this.formacao = formacao
        this.dataNascimento = dataNascimento
        this.competencias = competencias
    }

    String formatarCompetencias() {
        competencias ? competencias.join(", ") : "Nenhuma competência cadastrada"
    }

    @Override
    void exibirDados() {
        println "\nCandidato: ${nome} ${sobrenome}"
        println "Data de nascimento: ${dataNascimento}"
        println "email: ${email}"
        println "CPF: ${cpf}"
        println "País: ${pais}"
        println "CEP: ${cep}"
        println "Descrição: ${descricao}"
        println "Formação: ${formacao}"
        println "Competências: ${formatarCompetencias()}"
    }
}
