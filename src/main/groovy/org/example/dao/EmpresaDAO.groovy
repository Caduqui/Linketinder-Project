package org.example.dao

import org.example.ConexaoBanco
import org.example.Empresa

import java.sql.Connection
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

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setString(1, empresa.nome)
        statement.setString(2, empresa.email)
        statement.setString(3, empresa.cnpj)
        statement.setString(4, empresa.pais)
        statement.setString(5, empresa.cep)
        statement.setString(6, empresa.descricao)
        statement.setString(7, empresa.senha)

        ResultSet resultado = statement.executeQuery()

        if (resultado.next()) {
            empresa.id = resultado.getInt("id")
        }

        resultado.close()
        statement.close()
        conexao.close()
    }

    List<Empresa> listar() {

        List<Empresa> empresas = []

        String sql = """
            SELECT * FROM empresas ORDER BY id
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)
        ResultSet resultado = statement.executeQuery()

        while (resultado.next()) {

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
            empresas << empresa
        }

        resultado.close()
        statement.close()
        conexao.close()

        return empresas
    }

    void atualizar(Empresa empresa) {

        String sql = """
            UPDATE empresas 
                SET nome = ?, email = ?, cnpj = ?, pais = ?, cep = ?, descricao = ?, senha = ?
                WHERE id = ?
            """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setString(1, empresa.nome)
        statement.setString(2, empresa.email)
        statement.setString(3, empresa.cnpj)
        statement.setString(4, empresa.pais)
        statement.setString(5, empresa.cep)
        statement.setString(6, empresa.descricao)
        statement.setString(7, empresa.senha)
        statement.setInt(8, empresa.id)

        statement.executeUpdate()
        statement.close()
        conexao.close()
    }

    void deletar(Integer id) {

        String sql = """
            DELETE FROM empresas
            WHERE id = ?
        """

        Connection conexao = ConexaoBanco.conectar();
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, id)
        statement.executeUpdate()
        statement.close()
        conexao.close()
    }

    Empresa buscarPorId(Integer id) {

        String sql = """
            SELECT * FROM empresas WHERE id = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, id)

        ResultSet resultado = statement.executeQuery()

        Empresa empresa = null

        if (resultado.next()) {
            empresa = new Empresa(
                    resultado.getString("nome"),
                    resultado.getString("email"),
                    resultado.getString("cnpj"),
                    resultado.getString("pais"),
                    resultado.getString("cep"),
                    resultado.getString("descricao"),
                    resultado.getString("senha")
            )

            empresa.id = resultado.getInt("id")
        }

        resultado.close()
        statement.close()
        conexao.close()

        return empresa
    }

    boolean possuiVagas(Integer idEmpresa) {

        String sql = """
            SELECT COUNT(*) AS quantidade
            FROM vagas
            WHERE id_empresa = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, idEmpresa)
        ResultSet resultado = statement.executeQuery()

        boolean possui = false

        if (resultado.next()) {
            possui = resultado.getInt("quantidade") > 0
        }

        resultado.close()
        statement.close()
        conexao.close()

        return possui
    }

}
