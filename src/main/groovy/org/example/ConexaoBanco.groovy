package org.example

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

/**
 *
 * @author Guilherme Lima Conte
 */

class ConexaoBanco {
    static final String URL = "jdbc:postgresql://localhost:5432/linketinder"
    static final String USUARIO = "postgres"
    static final String SENHA = "postgres"

    Connection conectar() {
        return DriverManager.getConnection(URL, USUARIO, SENHA)
    }

    def <T> T executar(String sql, Closure<T> acao) {
        conectar().withCloseable { Connection conexao ->
            conexao.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                acao(statement)
            }
        }
    }
}
