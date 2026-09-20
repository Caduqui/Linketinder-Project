package org.example.Cadastro

import org.example.Empresa
import org.example.dao.EmpresaDAO

class CadastroEmpresa implements CadastroCrud {

    final EmpresaDAO empresaDAO
    final EntradaDados entrada

    CadastroEmpresa(EntradaDados entrada, EmpresaDAO empresaDAO = new EmpresaDAO()) {
        this.entrada = entrada
        this.empresaDAO = empresaDAO
    }

    @Override
    void listar() {
        empresaDAO.listar().each {
            it.exibirDados()
        }
    }

    void listarNomes() {
        empresaDAO.listar().each {
            println "ID: ${it.id} - ${it.nome}"
        }
    }

    @Override
    boolean cadastrar() {
        empresaDAO.inserir(lerDadosEmpresa())
        println "Empresa cadastrada com sucesso!"
        return true
    }

    @Override
    boolean atualizar() {
        Empresa empresa = selecionarEmpresa("ID da empresa: ")
        if (empresa == null) {
            return false
        }

        Empresa atualizada = lerDadosEmpresa()
        atualizada.id = empresa.id
        empresaDAO.atualizar(atualizada)
        println "Empresa atualizada com sucesso!"
        return true
    }

    @Override
    boolean excluir() {
        Empresa empresa = selecionarEmpresa("ID da empresa: ")
        if (empresa == null) {
            return false
        }

        if (empresaDAO.possuiVagas(empresa.id)) {
            println "Não é possível excluir a empresa porque ela possui vagas cadastradas."
            return false
        }

        empresaDAO.deletar(empresa.id)
        println "Empresa excluída com sucesso!"
        return true
    }

    Empresa selecionarEmpresa(String mensagem) {
            Integer id = entrada.lerId(mensagem)
            if (id == null) {
                return null
            }

            Empresa empresa = empresaDAO.buscarPorId(id)
            if (empresa == null) {
                println "Empresa não encontrada"
            }

            return empresa
    }

    Empresa lerDadosEmpresa() {
        String nome = entrada.lerTexto("Nome: ")
        String email = entrada.lerTexto("Email: ")
        String pais = entrada.lerTexto("País: ")
        String cep =entrada.lerTexto("CEP: ")
        String descricao = entrada.lerTexto("Descrição: ")
        String cnpj = entrada.lerTexto("CNPJ: ")
        String senha = entrada.lerSenha()

        return new Empresa(nome, email, cnpj, pais, cep, descricao, senha)
    }

}
