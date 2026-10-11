package org.linketinder.cadastro.http

/**
 *
 * @author Guilherme Lima Conte
 */

class Requisicao {

    final String metodo
    final String caminho
    final String corpo

    Requisicao(String metodo, String caminho, String corpo) {
        this.metodo = metodo
        this.caminho = caminho
        this.corpo = corpo
    }
}
