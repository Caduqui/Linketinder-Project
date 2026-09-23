package org.example.service

import org.example.model.Candidato
import org.example.repositorio.CandidatoRepositorio

/**
 *
 * @author Guilherme Lima Conte
 */

class CandidatoService {

    private final CandidatoRepositorio candidatoRepositorio

    CandidatoService(CandidatoRepositorio candidatoRepositorio) {
        this.candidatoRepositorio = candidatoRepositorio
    }

    List<Candidato> listar() {
        return candidatoRepositorio.listar()
    }

    Candidato buscarPorId(Integer id) {
        return candidatoRepositorio.buscarPorId(id)
    }

    void cadastrar(Candidato candidato) {
        candidatoRepositorio.inserir(candidato)
    }

    void atualizar(Integer id, Candidato dados) {
        dados.id = id
        candidatoRepositorio.atualizar(dados)
    }

    void excluir(Integer id) {
        candidatoRepositorio.deletar(id)
    }
}




