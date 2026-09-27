package org.example.api

import org.example.Banco.ConexaoBanco
import org.example.dao.CandidatoDAO
import org.example.dao.CompetenciaDAO
import org.example.dao.EmpresaDAO
import org.example.dao.VagaDAO
import org.example.repositorio.CompetenciaRepositorio
import org.example.repositorio.EmpresaRepositorio
import org.example.service.CandidatoService
import org.example.service.EmpresaService
import org.example.service.VagaService

/**
 *
 * @author Guilherme Lima Conte
 */

class Dependencias {

    private static Dependencias instancia

    final CandidatoService candidatoService
    final EmpresaService empresaService
    final VagaService vagaService

    Dependencias (CandidatoService candidatoService, EmpresaService empresaService, VagaService vagaService) {
        this.candidatoService = candidatoService
        this.empresaService = empresaService
        this.vagaService = vagaService
    }

    private Dependencias() {
        ConexaoBanco conexaoBanco = ConexaoBanco.obterInstancia()
        CompetenciaRepositorio competenciaRepositorio = new CompetenciaDAO(conexaoBanco)
        EmpresaRepositorio empresaRepositorio = new EmpresaDAO(conexaoBanco)

        this.candidatoService = new CandidatoService(new CandidatoDAO(conexaoBanco, competenciaRepositorio))
        this.empresaService = new EmpresaService(empresaRepositorio)
        this.vagaService = new VagaService(new VagaDAO(conexaoBanco, competenciaRepositorio, empresaRepositorio))
    }

    static synchronized Dependencias obterInstancia() {
        if (instancia == null) {
            instancia = new Dependencias()
        }
        return instancia
    }
}
