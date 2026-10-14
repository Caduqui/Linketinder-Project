package org.example.repositorio

import org.example.model.Recrutador

/**
 *
 * @author Guilherme Lima Conte
 */
interface RecrutadorRepositorio {
    void inserir(Recrutador recrutador)
    void atualizar(Recrutador recrutador)
    void deletar(Integer id)
    Recrutador buscarPorId(Integer id)
    Recrutador buscarPorEmail(String email)
    Recrutador buscarPorCpf(String cpf)
    List<Recrutador> listarPorEmpresa(Integer idEmpresa)
}
