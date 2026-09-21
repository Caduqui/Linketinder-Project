package org.example.Cadastro

import org.example.Empresa
import org.example.repositorio.EmpresaRepositorio

/**
 *
 * @author Guilherme Lima Conte
 */

class CadastroEmpresa implements CadastroCrud {

    final EmpresaRepositorio empresaRepositorio
    final EntradaDados entrada

    CadastroEmpresa(EntradaDados entrada, EmpresaRepositorio empresaRepositorio) {
        this.entrada = entrada
        this.empresaRepositorio = empresaRepositorio
    }

    @Override
    void listar() {
        empresaRepositorio.listar().each {
            it.exibirDados()
        }
    }

    void listarNomes() {
        empresaRepositorio.listar().each {
            println "ID: ${it.id} - ${it.nome}"
        }
    }

    @Override
    boolean cadastrar() {
        empresaRepositorio.inserir(lerDadosEmpresa())
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
        empresaRepositorio.atualizar(atualizada)
        println "Empresa atualizada com sucesso!"
        return true
    }

    @Override
    boolean excluir() {
        Empresa empresa = selecionarEmpresa("ID da empresa: ")
        if (empresa == null) {
            return false
        }

        if (empresaRepositorio.possuiVagas(empresa.id)) {
            println "Não é possível excluir a empresa porque ela possui vagas cadastradas."
            return false
        }

        empresaRepositorio.deletar(empresa.id)
        println "Empresa excluída com sucesso!"
        return true
    }

    Empresa selecionarEmpresa(String mensagem) {
            Integer id = entrada.lerId(mensagem)
            if (id == null) {
                return null
            }

            Empresa empresa = empresaRepositorio.buscarPorId(id)
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
