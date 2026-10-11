package org.linketinder.cadastro

import org.linketinder.cadastro.http.RegistroDoServidor
import org.linketinder.cadastro.http.Roteador
import org.linketinder.cadastro.http.ServidorCadastro

/**
 *
 * @author Guilherme Lima Conte
 */

class Main {

    static final int SAIDA_COM_ERRO = 1

    static void main(String[] args) {
        try {
            subir(System.getenv(), new RegistroDoServidor(System.out))
        } catch (ConfiguracaoInvalidaException | IOException e) {
            System.err.println("O microsserviço de cadastro não subiu: ${e.message}")
            System.exit(SAIDA_COM_ERRO)
        }
    }

    static ServidorCadastro subir(Map<String, String> ambiente, RegistroDoServidor registro) {
        Configuracao configuracao = Configuracao.aPartirDe(ambiente)

        ServidorCadastro servidor = abrirServidor(configuracao.porta, new Roteador(registro))
        servidor.iniciar()
        registro.informar("Microsserviço de cadastro disponível na porta ${servidor.porta}")

        return servidor
    }

    private static ServidorCadastro abrirServidor(int porta, Roteador roteador) {
        try {
            return new ServidorCadastro(porta, roteador)
        } catch (BindException e) {
            throw new ConfiguracaoInvalidaException("A porta ${porta} já está em uso. Escolha outra na variável de ambiente ${Configuracao.VARIAVEL_PORTA}")
        }
    }
}
