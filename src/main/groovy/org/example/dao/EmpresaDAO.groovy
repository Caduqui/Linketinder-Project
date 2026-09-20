package org.example.dao

import org.example.ConexaoBanco
import org.example.Empresa

import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 *
 * @author Guilherme Lima Conte
 */

class EmpresaDAO {

    void inserir(Empresa empresa) {
        String sql = """
            INSERT INTO empresas (nome, email, cnpj, pais, cep, descricao, senha)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            preencherDados(statement, empresa)
            statement.executeQuery().withCloseable { ResultSet resultado ->
                if (resultado.next()) {
                    empresa.id = resultado.getInt("id")
                }
            }
        }
    }

    List<Empresa> listar() {
        String sql = """
            SELECT * FROM empresas ORDER BY id
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.executeQuery().withCloseable { ResultSet resultado ->
                List<Empresa> empresas = []
                while (resultado.next()) {
                    empresas << mapearEmpresa(resultado)
                }
                return empresas
            }
        }
    }

    void atualizar(Empresa empresa) {
        String sql = """
            UPDATE empresas 
                SET nome = ?, email = ?, cnpj = ?, pais = ?, cep = ?, descricao = ?, senha = ?
                WHERE id = ?
            """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            preencherDados(statement, empresa)
            statement.setInt(8, empresa.id)
            statement.executeUpdate()
        }
    }

    void deletar(Integer id) {
        ConexaoBanco.executar("DELETE FROM empresas WHERE id = ?") { PreparedStatement statement ->
            statement.setInt(1, id)
            statement.executeUpdate()
        }
    }

    Empresa buscarPorId(Integer id) {
        String sql = """
            SELECT * FROM empresas WHERE id = ?
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setInt(1, id)
            statement.executeQuery().withCloseable { ResultSet resultado ->
                resultado.next() ? mapearEmpresa(resultado) : null
            }
        }
    }

    boolean possuiVagas(Integer idEmpresa) {

        String sql = """
            SELECT EXISTS 
                (SELECT 1 FROM vagas WHERE id_empresa = ?) AS possui
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setInt(1, idEmpresa)
            statement.executeQuery().withCloseable { ResultSet resultado ->
                resultado.next() && resultado.getBoolean("possui")
            }
        }
    }

    static void preencherDados(PreparedStatement statement, Empresa empresa) {
        statement.setString(1, empresa.nome)
        statement.setString(2, empresa.email)
        statement.setString(3, empresa.cnpj)
        statement.setString(4, empresa.pais)
        statement.setString(5, empresa.cep)
        statement.setString(6, empresa.descricao)
        statement.setString(7, empresa.senha)
    }

    static Empresa mapearEmpresa(ResultSet resultado) {
        Empresa empresa = new Empresa(
                resultado.getString("nome"),
                resultado.getString("email"),
                resultado.getString("cnpj"),
                resultado.getString("pais"),
                resultado.getString("cep"),
                resultado.getString("descricao"),
                resultado.getString("senha")
        )
        empresa.id = resultado.getInt("id")
        return empresa
    }
}
