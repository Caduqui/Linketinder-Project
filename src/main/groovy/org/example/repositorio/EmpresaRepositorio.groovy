package org.example.repositorio

import org.example.Modelo.Empresa

/**
 *
 * @author Guilherme Lima Conte
 */

interface EmpresaRepositorio {
    void inserir(Empresa empresa)
    void atualizar(Empresa empresa)
    void deletar(Integer id)
    Empresa buscarPorId(Integer id)
    List<Empresa> listar()
    boolean possuiVagas(Integer idEmpresa)
}