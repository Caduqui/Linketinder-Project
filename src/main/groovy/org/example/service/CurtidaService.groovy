package org.example.service

import org.example.model.Match
import org.example.repositorio.CurtidaRepositorio
import org.example.repositorio.MatchRepositorio

/**
 *
 * @author Guilherme Lima Conte
 */

class CurtidaService {

    private final CurtidaRepositorio curtidaRepositorio
    private final MatchRepositorio matchRepositorio

    CurtidaService(CurtidaRepositorio curtidaRepositorio, MatchRepositorio matchRepositorio) {
        this.curtidaRepositorio = curtidaRepositorio
        this.matchRepositorio = matchRepositorio
    }

    boolean candidatoCurtirVaga(Integer idCandidato, Integer idVaga) {
        return curtidaRepositorio.candidatoCurtirVaga(idCandidato, idVaga)
    }

    boolean empresaCurtirCandidato(Integer idEmpresa, Integer idCandidato, Integer idVaga) {
        return curtidaRepositorio.empresaCurtirCandidato(idEmpresa, idCandidato, idVaga)
    }

    boolean houveMatch(Integer idCandidato, Integer idEmpresa, Integer idVaga) {
        return matchRepositorio.criarMatchSePossivel(idCandidato, idEmpresa, idVaga)
    }

    List<Match> listarMatches() {
        return matchRepositorio.listar()
    }
}
