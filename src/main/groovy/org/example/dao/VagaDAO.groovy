package org.example.dao

import org.example.Competencia
import org.example.ConexaoBanco
import org.example.Empresa
import org.example.Vaga

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 *
 * @author Guilherme Lima Conte
 */

class VagaDAO {

    void inserir(Vaga vaga) {

        String sql = """
            INSERT INTO vagas (nome, descricao, estado, cidade, id_empresa) VALUES (?, ?, ?, ?, ?) RETURNING id
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setString(1, vaga.nome)
        statement.setString(2, vaga.descricao)
        statement.setString(3, vaga.estado)
        statement.setString(4, vaga.cidade)
        statement.setInt(5, vaga.empresa.id)

        ResultSet resultado = statement.executeQuery()

        if (resultado.next()) {
            vaga.id = resultado.getInt("id")
        }

        resultado.close()
        statement.close()
        conexao.close()

        CompetenciaDAO competenciaDAO = new CompetenciaDAO()

        vaga.competencias.each { nomeCompetencia ->

            Competencia competencia =
                    competenciaDAO.buscarPorNome(nomeCompetencia)

            if (competencia == null) {
                competencia = new Competencia(nomeCompetencia)
                competenciaDAO.inserir(competencia)
            }

            inserirRelacaoVagaCompetencia(
                    vaga.id,
                    competencia.id
            )
        }
    }

    void inserirRelacaoVagaCompetencia(
            Integer idVaga,
            Integer idCompetencia
    ) {

        String sql = """
            INSERT INTO vaga_competencia (id_vaga, id_competencia) VALUES (?, ?)
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, idVaga)
        statement.setInt(2, idCompetencia)

        statement.executeUpdate()

        statement.close()
        conexao.close()
    }

    List<String> buscarCompetenciasDaVaga(Integer idVaga) {

        List<String> competencias = []

        String sql = """
            SELECT comp.nome FROM competencias AS comp, vaga_competencia AS vc
            WHERE comp.id = vc.id_competencia
            AND vc.id_vaga = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, idVaga)

        ResultSet resultado = statement.executeQuery()

        while (resultado.next()) {
            competencias << resultado.getString("nome")
        }

        resultado.close()
        statement.close()
        conexao.close()

        return competencias
    }

    List<Vaga> listar() {

        List<Vaga> vagas = []

        String sql = """
            SELECT * FROM vagas ORDER BY id
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)
        ResultSet resultado = statement.executeQuery()

        EmpresaDAO empresaDAO = new EmpresaDAO()

        while (resultado.next()) {

            Empresa empresa =
                    empresaDAO.buscarPorId(
                            resultado.getInt("id_empresa")
                    )

            List<String> competencias =
                    buscarCompetenciasDaVaga(
                            resultado.getInt("id")
                    )

            Vaga vaga = new Vaga(
                    resultado.getString("nome"),
                    resultado.getString("descricao"),
                    resultado.getString("estado"),
                    resultado.getString("cidade"),
                    empresa,
                    competencias
            )

            vaga.id = resultado.getInt("id")

            vagas << vaga
        }

        resultado.close()
        statement.close()
        conexao.close()

        return vagas
    }

    void atualizar(Vaga vaga) {

        String sql = """
            UPDATE vagas
            SET nome = ?, descricao = ?, estado = ?, cidade = ?, id_empresa = ?
            WHERE id = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setString(1, vaga.nome)
        statement.setString(2, vaga.descricao)
        statement.setString(3, vaga.estado)
        statement.setString(4, vaga.cidade)
        statement.setInt(5, vaga.empresa.id)
        statement.setInt(6, vaga.id)

        statement.executeUpdate()

        statement.close()
        conexao.close()

        removerCompetenciasDaVaga(vaga.id)

        CompetenciaDAO competenciaDAO = new CompetenciaDAO()

        vaga.competencias.each { nomeCompetencia ->

            Competencia competencia =
                    competenciaDAO.buscarPorNome(nomeCompetencia)

            if (competencia == null) {
                competencia = new Competencia(nomeCompetencia)
                competenciaDAO.inserir(competencia)
            }

            inserirRelacaoVagaCompetencia(
                    vaga.id,
                    competencia.id
            )
        }
    }

    void removerCompetenciasDaVaga(Integer idVaga) {

        String sql = """
            DELETE FROM vaga_competencia
            WHERE id_vaga = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, idVaga)

        statement.executeUpdate()

        statement.close()
        conexao.close()
    }

    void deletar(Integer id) {

        removerCompetenciasDaVaga(id)

        String sql = """
            DELETE FROM vagas
            WHERE id = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, id)

        statement.executeUpdate()

        statement.close()
        conexao.close()
    }


    Vaga buscarPorId(Integer id) {

        String sql = """
            SELECT * FROM vagas
            WHERE id = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, id)
        ResultSet resultado = statement.executeQuery()

        Vaga vaga = null

        if (resultado.next()) {

            EmpresaDAO empresaDAO = new EmpresaDAO()

            Empresa empresa = empresaDAO.buscarPorId(resultado.getInt("id_empresa"))
            List<String> competencias = buscarCompetenciasDaVaga(id)
            vaga = new Vaga(
                    resultado.getString("nome"),
                    resultado.getString("descricao"),
                    resultado.getString("estado"),
                    resultado.getString("cidade"),
                    empresa,
                    competencias
            )
            vaga.id = resultado.getInt("id")
        }

        resultado.close()
        statement.close()
        conexao.close()

        return vaga
    }
}
