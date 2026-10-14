package org.example.service

import org.example.model.Recrutador
import org.example.repositorio.RecrutadorRepositorio

/**
 *
 * @author Guilherme Lima Conte
 */
class RecrutadorService {

    private final RecrutadorRepositorio recrutadorRepositorio

    RecrutadorService(RecrutadorRepositorio recrutadorRepositorio) {
        this.recrutadorRepositorio = recrutadorRepositorio
    }

    List<Recrutador> listarPorEmpresa(Integer idEmpresa) {
        return recrutadorRepositorio.listarPorEmpresa(idEmpresa)
    }

    Recrutador buscarPorId(Integer id) {
        return recrutadorRepositorio.buscarPorId(id)
    }

    void cadastrar(Recrutador recrutador) {
        validar(recrutador)
        validarUnicidade(recrutador, null)
        recrutadorRepositorio.inserir(recrutador)
    }

    Recrutador autenticar(String email, String senha) {
        Recrutador recrutador = recrutadorRepositorio.buscarPorEmail(email)

        if (recrutador == null || recrutador.senha != senha) {
            return null
        }

        return recrutador
    }

    void atualizar(Integer id, Recrutador dados) {
        validar(dados)
        dados.id = id
        validarUnicidade(dados, id)
        recrutadorRepositorio.atualizar(dados)
    }

    void excluir(Integer id) {
        recrutadorRepositorio.deletar(id)
    }

    private void validar(Recrutador recrutador) {
        recrutador.nome = ValidadorDeRecrutador.validarNome(recrutador.nome)
        recrutador.area = ValidadorDeRecrutador.validarArea(recrutador.area)
        recrutador.descricao = ValidadorDeRecrutador.validarDescricao(recrutador.descricao)
        recrutador.email = ValidadorDeRecrutador.validarEmail(recrutador.email)
        recrutador.cpf = ValidadorDeRecrutador.validarCpf(recrutador.cpf)
        recrutador.dataNascimento = ValidadorDeRecrutador.validarDataNascimento(recrutador.dataNascimento?.toString())
        recrutador.senha = ValidadorDeRecrutador.validarSenha(recrutador.senha)
    }

    private void validarUnicidade(Recrutador recrutador, Integer idIgnorado) {
        Recrutador porEmail = recrutadorRepositorio.buscarPorEmail(recrutador.email)
        Recrutador porCpf = recrutadorRepositorio.buscarPorCpf(recrutador.cpf)

        boolean emailEmUso = porEmail != null && (idIgnorado == null || porEmail.id != idIgnorado)
        boolean cpfEmUso = porCpf != null && (idIgnorado == null || porCpf.id != idIgnorado)

        if (emailEmUso || cpfEmUso) {
            throw new RegraDeNegocioException("Já existe um recrutador com esses dados")
        }
    }
}
