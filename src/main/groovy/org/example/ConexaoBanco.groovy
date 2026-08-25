package org.example

import java.sql.Connection
import java.sql.DriverManager

/**
 *
 * @author Guilherme Lima Conte
 */

class ConexaoBanco {
    static Connection conectar() {
        String url = "jdbc:postgresql://localhost:5432/linketinder"
        String usuario = "postgres"
        String senha = "postgres"

        return DriverManager.getConnection(url, usuario, senha)
    }
}
