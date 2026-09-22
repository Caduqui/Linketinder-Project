package org.example.Banco

import java.sql.Connection
import java.sql.DriverManager

/**
 *
 * @author Guilherme Lima Conte
 */

class ProvedorMySQL implements ProvedorConexao {
    static final String URL = "jdbc:mysql://localhost:3306/linketinder"
    static final String USUARIO = "root"
    static final String SENHA = "root"

    @Override
    Connection abrirConexao() {
        return DriverManager.getConnection(URL, USUARIO, SENHA)
    }
}
