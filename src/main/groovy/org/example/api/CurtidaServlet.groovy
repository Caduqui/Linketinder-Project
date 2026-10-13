package org.example.api

import org.example.model.Candidato
import org.example.model.Vaga
import org.example.service.RegraDeNegocioException

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author Guilherme Lima Conte
 */

@WebServlet(name = "curtidas", urlPatterns = ["/curtidas"])
class CurtidaServlet extends ApiServlet {

    static final String TIPO_CANDIDATO = "candidato"
    static final String TIPO_EMPRESA = "empresa"

    @Override
    protected void doPost(HttpServletRequest requisicao, HttpServletResponse resposta) {
        Map dados = lerCorpo(requisicao)
        String tipo = Json.exigirTexto(dados, "tipo")
        Integer idCandidato = Json.exigirInteiro(dados, "idCandidato")
        Integer idVaga = Json.exigirInteiro(dados, "idVaga")

        Candidato candidato = dependencias.candidatoService.buscarPorId(idCandidato)
        if (candidato == null) {
            responderNaoEncontrado(resposta, "Candidato ${idCandidato} não encontrado")
            return
        }

        Vaga vaga = dependencias.vagaService.buscarPorId(idVaga)
        if (vaga == null) {
            responderNaoEncontrado(resposta, "Vaga ${idVaga} não encontrada")
            return
        }

        registrarCurtida(tipo, dados, candidato, vaga)

        boolean houveMatch = dependencias.curtidaService.houveMatch(candidato.id, vaga.empresa.id, vaga.id)
        responder(resposta, HttpServletResponse.SC_CREATED, [match: houveMatch])


    }

    @Override
    protected void doGet(HttpServletRequest requisicao, HttpServletResponse resposta) {
        resposta.setHeader("Allow", "POST")
        responderErro(resposta, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "As curtidas só aceitam o método POST")
    }

    private void registrarCurtida(String tipo, Map dados, Candidato candidato, Vaga vaga) {
        if (tipo == TIPO_CANDIDATO) {
            dependencias.curtidaService.candidatoCurtirVaga(candidato.id, vaga.id)
            return
        }

        if (tipo == TIPO_EMPRESA) {
            Integer idEmpresa = Json.exigirInteiro(dados, "idEmpresa")

            if (vaga.empresa.id != idEmpresa) {
                throw new RegraDeNegocioException("A vaga ${vaga.id} não pertence à empresa ${idEmpresa}")
            }

            dependencias.curtidaService.empresaCurtirCandidato(idEmpresa, candidato.id, vaga.id)
            return
        }

        throw new RegraDeNegocioException("O campo 'tipo' deve ser '${TIPO_CANDIDATO}' ou '${TIPO_EMPRESA}'")
    }
}