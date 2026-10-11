package org.linketinder.cadastro.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

/**
 *
 * @author Guilherme Lima Conte
 */

class Roteador implements HttpHandler {

    static final int NAO_ENCONTRADO = 404
    static final int METODO_NAO_PERMITIDO = 405
    static final int ERRO_INTERNO = 500
    static final String ROTA_DESCONHECIDA = "(rota desconhecida)"

    private final RegistroDoServidor registro
    private final Map<String, Map<String, Manipulador>> manipuladoresPorCaminho = [:]

    Roteador(RegistroDoServidor registro) {
        this.registro = registro
    }

    void registrar(String metodo, String caminho, Manipulador manipulador) {
        if (!manipuladoresPorCaminho.containsKey(caminho)) {
            manipuladoresPorCaminho[caminho] = [:]
        }
        manipuladoresPorCaminho[caminho][metodo] = manipulador
    }

    @Override
    void handle(HttpExchange troca) {
        try {
            Requisicao requisicao = lerRequisicao(troca)
            Map<String, Manipulador> manipuladoresPorMetodo = manipuladoresPorCaminho[requisicao.caminho]

            RespostaJson resposta = responder(requisicao, manipuladoresPorMetodo)
            resposta.enviar(troca)

            String rota = manipuladoresPorMetodo == null ? ROTA_DESCONHECIDA : requisicao.caminho
            registro.registrarRequisicao(requisicao.metodo, rota, resposta.status)
        } finally {
            troca.close()
        }
    }

    private static Requisicao lerRequisicao(HttpExchange troca) {
        return new Requisicao(
                troca.requestMethod,
                troca.requestURI.path,
                troca.requestBody.getText(RespostaJson.CODIFICACAO)
        )
    }

    private static RespostaJson responder(Requisicao requisicao, Map<String, Manipulador> manipuladoresPorMetodo) {
        if (manipuladoresPorMetodo == null) {
            return RespostaJson.erro(NAO_ENCONTRADO, "Rota não encontrada")
        }

        Manipulador manipulador = manipuladoresPorMetodo[requisicao.metodo]
        if (manipulador == null) {
            return responderMetodoNaoPermitido(requisicao, manipuladoresPorMetodo.keySet())
        }

        try {
            return manipulador.atender(requisicao)
        } catch (Exception e) {
            return RespostaJson.erro(ERRO_INTERNO, "Erro interno do servidor")
        }
    }

    private static RespostaJson responderMetodoNaoPermitido(Requisicao requisicao, Set<String> metodosAceitos) {
        String aceitos = metodosAceitos.sort().join(", ")

        return RespostaJson
                .erro(METODO_NAO_PERMITIDO, "O método ${requisicao.metodo} não é suportado nesta rota. Use ${aceitos}")
                .comCabecalho("Allow", aceitos)
    }
}
