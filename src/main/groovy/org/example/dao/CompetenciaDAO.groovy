package org.example.dao

import org.example.Competencia
import org.example.ConexaoBanco

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

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setString(1, competencia.nome)
            statement.executeQuery().withCloseable { ResultSet resultado ->
                if (resultado.next()) {
                    competencia.id = resultado.getInt("id")
                }
            }
        }
    }

    List<Competencia> listar() {
        String sql = """
            SELECT * FROM competencias ORDER BY id
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.executeQuery().withCloseable { ResultSet resultado ->
                List<Competencia> competencias = []
                while (resultado.next()) {
                    competencias << mapearCompetencia(resultado)
                }
                return competencias
            }
        }
    }

    void atualizar(Competencia competencia) {
        String sql = """
            UPDATE competencias 
                SET nome = ?
                WHERE id = ? 
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setString(1, competencia.nome)
            statement.setInt(2, competencia.id)
            statement.executeUpdate()
        }
    }

    void deletar(Integer id) {
        String sql = """
            DELETE FROM competencias
                WHERE id = ?
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setInt(1, id)
            statement.executeUpdate()
        }
    }

    Competencia buscarPorId(Integer id) {
        String sql = """
            SELECT * FROM competencias WHERE id = ?
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setInt(1, id)
            buscarUma(statement)
        }
    }

    Competencia buscarPorNome(String nome) {
        String sql = """
            SELECT * FROM competencias WHERE nome = ?
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setString(1, nome)
            buscarUma(statement)
        }
    }

    Competencia buscarOuInserir(String nome) {
        Competencia competencia = buscarPorNome(nome)

        if (competencia == null) {
            competencia = new Competencia(nome)
            inserir(competencia)
        }

        return competencia
    }

    boolean estaVinculadoACandidatoOuVaga (Integer id) {
        String sql = """
            SELECT EXISTS (SELECT 1 FROM candidato_competencia WHERE id_competencia = ?)
                OR EXISTS (SELECT 1 FROM vaga_competencia WHERE id_competencia = ?) AS vinculada
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setInt(1, id)
            statement.setInt(2, id)
            statement.executeQuery().withCloseable { ResultSet resultado ->
                resultado.next() && resultado.getBoolean("vinculada")
            }
        }
    }

    private static Competencia buscarUma(PreparedStatement statement) {
        statement.executeQuery().withCloseable { ResultSet resultado ->
            resultado.next() ? mapearCompetencia(resultado) : null
        }
    }

    private static Competencia mapearCompetencia(ResultSet resultado) {
        Competencia competencia = new Competencia(resultado.getString("nome"))
        competencia.id = resultado.getInt("id")
        return competencia
    }
}
