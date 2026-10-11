package org.linketinder.cadastro.http

/**
 *
 * @author Guilherme Lima Conte
 */

class RegistroDoServidor {

    private final PrintStream saida

    RegistroDoServidor(PrintStream saida) {
        this.saida = saida
    }

    void registrarRequisicao(String metodo, String rota, int status) {
        saida.println("${metodo} ${rota} ${status}")
    }

    void informar(String mensagem) {
        saida.println(mensagem)
    }
}
