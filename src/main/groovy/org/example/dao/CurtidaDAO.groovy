package org.example.dao

import org.example.ConexaoBanco

import java.sql.Connection
import java.sql.PreparedStatement

class CurtidaDAO {

    boolean candidatoCurtirVaga(Integer idCandidato, Integer idVaga) {

        String sql = """
            INSERT INTO curtida_candidato_vaga (id_candidato, id_vaga) VALUES (?,?)
            ON CONFLICT (id_candidato, id_vaga) DO NOTHING
        """

        Connection conexao = ConexaoBanco.conectar();
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, idCandidato)
        statement.setInt(2, idVaga)

        int linhasAfetadas = statement.executeUpdate()

        statement.close()
        conexao.close()

        return linhasAfetadas > 0

    }

    boolean empresaCurtirCandidato(Integer idEmpresa, Integer idCandidato, Integer idVaga) {
        String sql = """
            INSERT INTO curtida_empresa_candidato (id_empresa, id_candidato, id_vaga) VALUES (?,?,?)
            ON CONFLICT (id_empresa, id_candidato, id_vaga) DO NOTHING
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, idEmpresa)
        statement.setInt(2, idCandidato)
        statement.setInt(3, idVaga)

        int linhasAfetadas = statement.executeUpdate()

        statement.close()
        conexao.close()

        return linhasAfetadas > 0
    }


}


