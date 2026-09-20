package org.example.dao

import org.example.ConexaoBanco

import java.sql.PreparedStatement

class CurtidaDAO {

    boolean candidatoCurtirVaga(Integer idCandidato, Integer idVaga) {
        String sql = """
            INSERT INTO curtida_candidato_vaga (id_candidato, id_vaga) VALUES (?,?)
            ON CONFLICT (id_candidato, id_vaga) DO NOTHING
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setInt(1, idCandidato)
            statement.setInt(2, idVaga)
            statement.executeUpdate() > 0
        }
    }

    boolean empresaCurtirCandidato(Integer idEmpresa, Integer idCandidato, Integer idVaga) {
        String sql = """
            INSERT INTO curtida_empresa_candidato (id_empresa, id_candidato, id_vaga) VALUES (?,?,?)
            ON CONFLICT (id_empresa, id_candidato, id_vaga) DO NOTHING
        """

        ConexaoBanco.executar(sql) { PreparedStatement statement ->
            statement.setInt(1, idEmpresa)
            statement.setInt(2, idCandidato)
            statement.setInt(3, idVaga)
            statement.executeUpdate() > 0
        }
    }
}
