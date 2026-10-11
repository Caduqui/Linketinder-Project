package org.linketinder.cadastro.http

import org.linketinder.cadastro.service.CadastroCandidatoService
import org.linketinder.cadastro.service.CadastroEmpresaService

/**
 *
 * @author Guilherme Lima Conte
 */

class RotasDeCadastro {

    static void registrar(Roteador roteador, CadastroCandidatoService cadastroCandidatoService, CadastroEmpresaService cadastroEmpresaService) {
        roteador.registrar("POST", "/candidatos", new CandidatoHandler(cadastroCandidatoService))
        roteador.registrar("POST", "/empresas", new EmpresaHandler(cadastroEmpresaService))
    }
}
