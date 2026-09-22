package org.example.Banco

import java.sql.Connection
import java.sql.DriverManager

/**
 *
 * @author Guilherme Lima Conte
 */

class ProvedorPostgreSQL implements ProvedorConexao{
    static final String URL = "jdbc:postgresql://localhost:5432/linketinder"
    static final String USUARIO = "postgres"
    static final String SENHA = "postgres"

    @Override
    Connection abrirConexao() {
        return DriverManager.getConnection(URL, USUARIO, SENHA)
    }
}
