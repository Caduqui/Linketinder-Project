package org.example.dao

import org.example.ConexaoBanco
import org.example.Match

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class MatchDAO {

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

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, idCandidato)
        statement.setInt(2, idEmpresa)
        statement.setInt(3, idVaga)

        int linhasAfetadas = statement.executeUpdate()

        statement.close()
        conexao.close()

        return linhasAfetadas > 0
    }

    List<Match> listar() {

        List<Match> matches = []

        String sql = """
            SELECT * FROM matches ORDER BY id
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)
        ResultSet resultado = statement.executeQuery()

        CandidatoDAO candidatoDAO = new CandidatoDAO()
        EmpresaDAO empresaDAO = new EmpresaDAO()
        VagaDAO vagaDAO = new VagaDAO()

        while (resultado.next()) {
            def candidato = candidatoDAO.buscarPorId(resultado.getInt("id_candidato"))
            def empresa = empresaDAO.buscarPorId(resultado.getInt("id_empresa"))
            def vaga = vagaDAO.buscarPorId(resultado.getInt("id_vaga"))

            matches << new Match(candidato, empresa, vaga)
        }

        resultado.close()
        statement.close()
        conexao.close()

        return matches
    }
}
