package org.linketinder.cadastro.banco

import org.linketinder.cadastro.Configuracao

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

/**
 *
 * @author Guilherme Lima Conte
 */

class ConexaoBanco {

    private final Configuracao configuracao

    ConexaoBanco(Configuracao configuracao) {
        this.configuracao = configuracao
    }

    Connection abrir() {
        return DriverManager.getConnection(configuracao.bancoUrl, configuracao.bancoUsuario, configuracao.bancoSenha)
    }

    def <T> T executar(String sql, Closure<T> acao) {
        abrir().withCloseable { Connection conexao ->
            conexao.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                acao(statement)
            }
        }
    }
}
