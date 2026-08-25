package org.example.dao

import org.example.Competencia
import org.example.ConexaoBanco

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 *
 * @author Guilherme Lima Conte
 */

class CompetenciaDAO {

    void inserir(Competencia competencia) {

        String sql = """
            INSERT INTO competencias (nome)
            VALUES (?)
            RETURNING id
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)
        statement.setString(1, competencia.nome)
        ResultSet resultado = statement.executeQuery()

        if (resultado.next()) {
            competencia.id = resultado.getInt("id")
        }

        resultado.close()
        statement.close()
        conexao.close()
    }

    List<Competencia> listar() {

        List<Competencia> competencias = []

        String sql = """
            SELECT * FROM competencias ORDER BY id
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)
        ResultSet resultado = statement.executeQuery()

        while (resultado.next()) {

            Competencia competencia = new Competencia(resultado.getString("nome"))
            competencia.id = resultado.getInt("id")
            competencias << competencia
        }

        resultado.close()
        statement.close()
        conexao.close()

        return competencias
    }

    void atualizar(Competencia competencia) {

        String sql = """
            UPDATE competencias 
                SET nome = ?
                WHERE id = ? 
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setString(1, competencia.nome)
        statement.setInt(2, competencia.id)
        statement.executeUpdate()
        statement.close()
        conexao.close()
    }

    void deletar(Integer id) {

        String sql = """
            DELETE FROM competencias
                WHERE id = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, id)
        statement.executeUpdate()

        statement.close()
        conexao.close()
    }

    Competencia buscarPorNome(String nome) {
        String sql = """
            SELECT * FROM competencias WHERE nome = ?
        """

        Connection conexao  = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setString(1, nome)

        ResultSet resultado = statement.executeQuery()

        Competencia competencia = null

        if (resultado.next()) {
            competencia = new Competencia(resultado.getString("nome"))
            competencia.id = resultado.getInt("id")
        }

        resultado.close()
        statement.close()
        conexao.close()

        return competencia
    }

    Competencia buscarPorId(Integer id) {

        String sql = """
            SELECT * FROM competencias WHERE id = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, id)
        ResultSet resultado = statement.executeQuery()

        Competencia competencia = null

        if (resultado.next()) {
            competencia = new Competencia(resultado.getString("nome"))
            competencia.id = resultado.getInt("id")
        }

        resultado.close()
        statement.close()
        conexao.close()

        return competencia
    }

    // impede de excluir uma competencia se ela estiver associado a um candidato
    boolean estaEmUso (Integer id) {
        String sql = """
            SELECT
                (SELECT COUNT(*) FROM candidato_competencia
                WHERE id_competencia = ?) +
                (SELECT COUNT(*) FROM vaga_competencia
                WHERE id_competencia = ?) AS quantidade
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, id)
        statement.setInt(2, id)

        ResultSet resultado = statement.executeQuery()

        boolean emUso = false

        if (resultado.next()) {
            emUso = resultado.getInt("quantidade") > 0
        }

        resultado.close()
        statement.close()
        conexao.close()

        return emUso
    }

}
