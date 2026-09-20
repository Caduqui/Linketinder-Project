package org.example.dao

import org.example.Competencia
import org.example.ConexaoBanco
import org.example.Vaga

import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 *
 * @author Guilherme Lima Conte
 */

class VagaDAO {

    private final CompetenciaDAO competenciaDAO = new CompetenciaDAO()
    private final EmpresaDAO empresaDAO = new EmpresaDAO()

    void inserir(Vaga vaga) {

        String sql = """
            INSERT INTO vagas (nome, descricao, estado, cidade, id_empresa) VALUES (?, ?, ?, ?, ?) RETURNING id
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            preencherDados(statement, vaga)
            statement.executeQuery().withCloseable { ResultSet resultado ->
                if (resultado.next()) {
                    vaga.id = resultado.getInt("id")
                }
            }
        }

        salvarCompetencias(vaga)
    }

    void atualizar(Vaga vaga) {

        String sql = """
            UPDATE vagas
            SET nome = ?, descricao = ?, estado = ?, cidade = ?, id_empresa = ?
            WHERE id = ?
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            preencherDados(statement, vaga)
            statement.setInt(6, vaga.id)
            statement.executeUpdate()
        }

        removerCompetenciasDaVaga(vaga.id)
        salvarCompetencias(vaga)
    }

    void deletar(Integer id) {
        ["vaga_competencia", "curtida_candidato_vaga", "curtida_empresa_candidato", "matches"].each { String tabela ->
            removerRegistrosDaVaga(tabela, id)
        }

        ConexaoBanco.executar("DELETE FROM vagas WHERE id = ?") { PreparedStatement statement ->
            statement.setInt(1, id)
            statement.executeUpdate()
        }
    }

    List<Vaga> listar() {
        return buscarVagas("SELECT * FROM vagas ORDER BY id")
    }

    List<Vaga> listarPorEmpresa(Integer idEmpresa) {
        return buscarVagas("SELECT * FROM vagas WHERE id_empresa = ? ORDER BY id", idEmpresa)
    }

    Vaga buscarPorId(Integer id) {
        List<Vaga> vagas = buscarVagas("SELECT * FROM vagas WHERE id = ?", id)
        return vagas ? vagas.first() : null
    }

    List<Vaga> buscarVagas( String sql, Integer parametro = null ) {
        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            if (parametro != null) {
                statement.setInt(1, parametro)
            }
            statement.executeQuery().withCloseable { ResultSet resultado ->
                List<Vaga> vagas = []
                while (resultado.next()) {
                    vagas << mapearVaga(resultado)
                }
                return vagas
            }
        }
    }

    List<String> buscarCompetenciasDaVaga(Integer idVaga) {
        String sql = """
            SELECT comp.nome FROM competencias AS comp, vaga_competencia AS vc
            WHERE comp.id = vc.id_competencia
            AND vc.id_vaga = ?
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setInt(1, idVaga)
            statement.executeQuery().withCloseable { ResultSet resultado ->
                List<String> competencias = []
                while (resultado.next()) {
                    competencias << resultado.getString("nome")
                }
                return competencias
            }
        }
    }

    void removerCompetenciasDaVaga(Integer idVaga) {
        removerRegistrosDaVaga("vaga_competencia", idVaga)
    }

    void inserirRelacaoVagaCompetencia( Integer idVaga, Integer idCompetencia) {
        String sql = """
            INSERT INTO vaga_competencia (id_vaga, id_competencia) VALUES (?, ?)
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setInt(1, idVaga)
            statement.setInt(2, idCompetencia)
            statement.executeUpdate()
        }
    }

    void salvarCompetencias(Vaga vaga) {
        vaga.competencias.each { String nomeCompetencia ->
            Competencia competencia = competenciaDAO.buscarOuInserir(nomeCompetencia)
            inserirRelacaoVagaCompetencia(vaga.id, competencia.id)
        }
    }

    static void removerRegistrosDaVaga(String tabela, Integer idVaga) {
        ConexaoBanco.executar("DELETE FROM ${tabela} WHERE id_vaga = ?") { PreparedStatement statement ->
            statement.setInt(1, idVaga)
            statement.executeUpdate()
        }
    }

    static void preencherDados(PreparedStatement statement, Vaga vaga) {
        statement.setString(1, vaga.nome)
        statement.setString(2, vaga.descricao)
        statement.setString(3, vaga.estado)
        statement.setString(4, vaga.cidade)
        statement.setInt(5, vaga.empresa.id)
    }

    Vaga mapearVaga(ResultSet resultado) {
        Integer id = resultado.getInt("id")

        Vaga vaga = new Vaga(
                resultado.getString("nome"),
                resultado.getString("descricao"),
                resultado.getString("estado"),
                resultado.getString("cidade"),
                empresaDAO.buscarPorId(resultado.getInt("id_empresa")),
                buscarCompetenciasDaVaga(id)
        )
        vaga.id = id
        return vaga
    }
}
