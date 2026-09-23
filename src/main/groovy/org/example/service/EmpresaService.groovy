package org.example.service

import org.example.model.Empresa
import org.example.repositorio.EmpresaRepositorio

/**
 *
 * @author Guilherme Lima Conte
 */

class EmpresaService {

    private final EmpresaRepositorio empresaRepositorio

    EmpresaService(EmpresaRepositorio empresaRepositorio) {
        this.empresaRepositorio = empresaRepositorio
    }

    List<Empresa> listar() {
        return empresaRepositorio.listar()
    }

    Empresa buscarPorId(Integer id) {
        return empresaRepositorio.buscarPorId(id)
    }

    void cadastrar(Empresa empresa) {
        empresaRepositorio.inserir(empresa)
    }

    void atualizar(Integer id, Empresa dados) {
        dados.id = id
        empresaRepositorio.atualizar(dados)
    }

    void excluir(Integer id) {
        if (empresaRepositorio.possuiVagas(id)) {
            throw new RegraDeNegocioException("Não é possível excluir a empresa porque ela possui vagas cadastradas")
        }

        empresaRepositorio.deletar(id)
    }
}
