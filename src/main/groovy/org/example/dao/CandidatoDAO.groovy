package org.example.dao

import org.example.Candidato
import org.example.Competencia
import org.example.ConexaoBanco

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Date

/**
 *
 * @author Guilherme Lima Conte
 */

class CandidatoDAO {

    void inserir(Candidato candidato) {

        String sql = """
            INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao, senha, formacao)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setString(1, candidato.nome)
        statement.setString(2, candidato.sobrenome)
        statement.setDate(3, Date.valueOf(candidato.dataNascimento.toString()))
        statement.setString(4, candidato.email)
        statement.setString(5, candidato.cpf)
        statement.setString(6, candidato.pais)
        statement.setString(7, candidato.cep)
        statement.setString(8, candidato.descricao)
        statement.setString(9, candidato.senha)
        statement.setString(10, candidato.formacao)

        ResultSet resultado = statement.executeQuery()

        if (resultado.next()) {
            candidato.id = resultado.getInt("id")
        }

        resultado.close()
        statement.close()
        conexao.close()


        CompetenciaDAO competenciaDAO = new CompetenciaDAO()

        candidato.competencias.each { nomeCompetencia ->

            Competencia competencia =
                    competenciaDAO.buscarPorNome(nomeCompetencia)

            if (competencia == null) {
                competencia = new Competencia(nomeCompetencia)
                competenciaDAO.inserir(competencia)
            }

            inserirRelacaoCandidatoCompetencia(
                    candidato.id,
                    competencia.id
            )
        }
    }

    Candidato buscarPorId(Integer id) {

        String sql = """
            SELECT * FROM candidatos WHERE id = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, id)
        ResultSet resultado = statement.executeQuery()
        Candidato candidato = null

        if (resultado.next()) {

            List<String> competencias = buscarCompetenciasDoCandidato(id)

            candidato = new Candidato(
                    resultado.getString("nome"),
                    resultado.getString("sobrenome"),
                    resultado.getDate("data_nascimento").toString(),
                    resultado.getString("email"),
                    resultado.getString("cpf"),
                    resultado.getString("pais"),
                    resultado.getString("cep"),
                    resultado.getString("descricao"),
                    resultado.getString("senha"),
                    resultado.getString("formacao"),
                    competencias
            )

            candidato.id = resultado.getInt("id")
        }

        resultado.close()
        statement.close()
        conexao.close()

        return candidato
    }

    void inserirRelacaoCandidatoCompetencia(
            Integer idCandidato,
            Integer idCompetencia
    ) {

        String sql = """
            INSERT INTO candidato_competencia (id_candidato, id_competencia) VALUES (?, ?)
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, idCandidato)
        statement.setInt(2, idCompetencia)

        statement.executeUpdate()

        statement.close()
        conexao.close()
    }

    List<Candidato> listar() {

        List<Candidato> candidatos = []

        String sql = """
            SELECT * FROM candidatos ORDER BY id
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)
        ResultSet resultado = statement.executeQuery()

        while (resultado.next()) {

            List<String> competencias =
                    buscarCompetenciasDoCandidato(
                            resultado.getInt("id")
                    )

            Candidato candidato = new Candidato(
                    resultado.getString("nome"),
                    resultado.getString("sobrenome"),
                    resultado.getDate("data_nascimento").toString(),
                    resultado.getString("email"),
                    resultado.getString("cpf"),
                    resultado.getString("pais"),
                    resultado.getString("cep"),
                    resultado.getString("descricao"),
                    resultado.getString("senha"),
                    resultado.getString("formacao"),
                    competencias
            )

            candidato.id = resultado.getInt("id")

            candidatos << candidato
        }

        resultado.close()
        statement.close()
        conexao.close()

        return candidatos
    }

    List<String> buscarCompetenciasDoCandidato(Integer idCandidato) {

        List<String> competencias = []

        String sql = """
            SELECT comp.nome FROM competencias AS comp, candidato_competencia AS cc
            WHERE comp.id = cc.id_competencia
            AND cc.id_candidato = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, idCandidato)

        ResultSet resultado = statement.executeQuery()

        while (resultado.next()) {
            competencias << resultado.getString("nome")
        }

        resultado.close()
        statement.close()
        conexao.close()

        return competencias
    }

    void atualizar(Candidato candidato) {

        String sql = """
            UPDATE candidatos
            SET nome = ?, sobrenome = ?, data_nascimento = ?, email = ?, cpf = ?, pais = ?, cep = ?, descricao = ?, senha = ?, formacao = ?
            WHERE id = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setString(1, candidato.nome)
        statement.setString(2, candidato.sobrenome)
        statement.setDate(3, Date.valueOf(candidato.dataNascimento.toString()))
        statement.setString(4, candidato.email)
        statement.setString(5, candidato.cpf)
        statement.setString(6, candidato.pais)
        statement.setString(7, candidato.cep)
        statement.setString(8, candidato.descricao)
        statement.setString(9, candidato.senha)
        statement.setString(10, candidato.formacao)
        statement.setInt(11, candidato.id)

        statement.executeUpdate()

        statement.close()
        conexao.close()


        // Remove as relações antigas
        removerCompetenciasDoCandidato(candidato.id)

        // Cadastra novamente as competências atuais
        CompetenciaDAO competenciaDAO = new CompetenciaDAO()

        candidato.competencias.each { nomeCompetencia ->

            Competencia competencia =
                    competenciaDAO.buscarPorNome(nomeCompetencia)

            if (competencia == null) {
                competencia = new Competencia(nomeCompetencia)
                competenciaDAO.inserir(competencia)
            }

            inserirRelacaoCandidatoCompetencia(
                    candidato.id,
                    competencia.id
            )
        }
    }

    void removerCompetenciasDoCandidato(Integer idCandidato) {

        String sql = """
            DELETE FROM candidato_competencia
            WHERE id_candidato = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, idCandidato)

        statement.executeUpdate()

        statement.close()
        conexao.close()
    }

    void deletar(Integer id) {

        removerCompetenciasDoCandidato(id)

        String sql = """
            DELETE FROM candidatos
            WHERE id = ?
        """

        Connection conexao = ConexaoBanco.conectar()
        PreparedStatement statement = conexao.prepareStatement(sql)

        statement.setInt(1, id)

        statement.executeUpdate()

        statement.close()
        conexao.close()
    }

}
