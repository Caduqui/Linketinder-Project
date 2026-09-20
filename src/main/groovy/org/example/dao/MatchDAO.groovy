package org.example.dao

import org.example.ConexaoBanco
import org.example.Match

import java.sql.PreparedStatement
import java.sql.ResultSet

class MatchDAO {

    final CandidatoDAO candidatoDAO = new CandidatoDAO()
    final EmpresaDAO empresaDAO = new EmpresaDAO()
    final VagaDAO vagaDAO = new VagaDAO()

    boolean criarMatchSePossivel(Integer idCandidato, Integer idEmpresa, Integer idVaga) {
        String sql = """
            INSERT INTO matches (id_candidato,id_empresa,id_vaga)
                SELECT ccv.id_candidato, cec.id_empresa, ccv.id_vaga
                FROM curtida_candidato_vaga AS ccv, curtida_empresa_candidato AS cec, vagas AS v
                WHERE ccv.id_candidato = ?
                AND cec.id_empresa = ?
                AND ccv.id_vaga = ?
                AND ccv.id_candidato = cec.id_candidato
                AND ccv.id_vaga = cec.id_vaga
                AND v.id = ccv.id_vaga
                AND v.id_empresa = cec.id_empresa
                ON CONFLICT (id_candidato,id_empresa,id_vaga) DO NOTHING
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setInt(1, idCandidato)
            statement.setInt(2, idEmpresa)
            statement.setInt(3, idVaga)
            statement.executeUpdate() > 0
        }
    }

    List<Match> listar() {
        String sql = """
            SELECT * FROM matches ORDER BY id
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.executeQuery().withCloseable { ResultSet resultado ->
                List<Match> matches = []
                while (resultado.next()) {
                    matches << mapearMatch(resultado)
                }
                return matches
            }
        }
    }

    Match mapearMatch(ResultSet resultado) {
        return new Match(
                candidatoDAO.buscarPorId(resultado.getInt("id_candidato")),
                empresaDAO.buscarPorId(resultado.getInt("id_empresa")),
                vagaDAO.buscarPorId(resultado.getInt("id_vaga"))
        )
    }
}
