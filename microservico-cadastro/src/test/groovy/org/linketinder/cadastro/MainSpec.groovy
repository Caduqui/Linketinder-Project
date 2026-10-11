package org.linketinder.cadastro

import org.linketinder.cadastro.http.RegistroDoServidor
import org.linketinder.cadastro.http.ServidorCadastro
import spock.lang.Specification

/**
 *
 * @author Guilherme Lima Conte
 */

class MainSpec extends Specification {

    static final String SENHA_FICTICIA = "senha-de-teste"
    static final String PORTA_LIVRE = "0"

    ByteArrayOutputStream saida = new ByteArrayOutputStream()
    RegistroDoServidor registro = new RegistroDoServidor(new PrintStream(saida, true, "UTF-8"))

    def "com a senha do banco informada o serviço sobe na porta configurada"() {
        when:
        ServidorCadastro servidor = Main.subir([
                (Configuracao.VARIAVEL_PORTA)      : PORTA_LIVRE,
                (Configuracao.VARIAVEL_BANCO_SENHA): SENHA_FICTICIA
        ], registro)

        then:
        servidor.porta > 0
        saida.toString("UTF-8").contains("${servidor.porta}")
        !saida.toString("UTF-8").contains(SENHA_FICTICIA)

        cleanup:
        servidor?.parar()
    }

    def "com a porta já em uso o serviço não sobe e diz qual porta e qual variável trocar"() {
        given:
        ServidorCadastro ocupante = Main.subir([
                (Configuracao.VARIAVEL_PORTA)      : PORTA_LIVRE,
                (Configuracao.VARIAVEL_BANCO_SENHA): SENHA_FICTICIA
        ], registro)

        when:
        Main.subir([
                (Configuracao.VARIAVEL_PORTA)      : "${ocupante.porta}".toString(),
                (Configuracao.VARIAVEL_BANCO_SENHA): SENHA_FICTICIA
        ], registro)

        then:
        ConfiguracaoInvalidaException erro = thrown()
        erro.message.contains("${ocupante.porta}")
        erro.message.contains(Configuracao.VARIAVEL_PORTA)

        cleanup:
        ocupante?.parar()
    }

    def "sem a senha do banco o serviço não sobe"() {
        when:
        Main.subir([(Configuracao.VARIAVEL_PORTA): PORTA_LIVRE], registro)

        then:
        ConfiguracaoInvalidaException erro = thrown()
        erro.message.contains(Configuracao.VARIAVEL_BANCO_SENHA)
    }
}
