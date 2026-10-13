import {obterUsuarioDoTipo} from "../sessao.ts";
import {mostrarMensagem} from "../dom.ts";
import {curtirCandidato, listarCandidatos, listarVagasDaEmpresa} from "../api/api.ts";
import {calcularAfinidade, type ComAfinidade, ordenarPorAfinidade} from "../afinidade.ts";
import type {Candidato, Vaga} from "../modelos.ts";
import {iniciarFilaDeCards} from "../filaDeCards.ts";

function lerIdDaVagaNaUrl(): number | null {
    const id = Number(new URLSearchParams(window.location.search).get("id"));
    return Number.isInteger(id) && id > 0 ? id : null;
}

export async function carregarCandidatosDaVaga(): Promise<void> {
    const empresa = obterUsuarioDoTipo("empresa");

    if (!empresa) {
        return;
    }

    const idVaga = lerIdDaVagaNaUrl();

    if (!idVaga) {
        mostrarMensagem("Vaga inválida. Volte para \"Minhas vagas\" e escolha uma vaga.");
        return;
    }

    const [vagasDaEmpresa, candidatos] = await Promise.all([listarVagasDaEmpresa(empresa.id), listarCandidatos()]);
    const vaga = vagasDaEmpresa.find(vagaDaEmpresa => vagaDaEmpresa.id === idVaga);

    if (!vaga) {
        mostrarMensagem("Esta vaga não existe ou não pertence à sua empresa.");
        return;
    }

    mostrarResumoDaVaga(vaga);

    const candidatosComAfinidade = ordenarPorAfinidade(candidatos,
        candidato => calcularAfinidade(candidato.competencias, vaga.competencias)
    ).filter(({afinidade}) => afinidade > 0);

    iniciarFilaDeCards<ComAfinidade<Candidato>>({
        itens: candidatosComAfinidade,
        montarCard: ({item: candidato, afinidade}) => ({
            titulo: `Candidato ${candidato.id}`,
            afinidade: afinidade,
            detalhes: [
                ["Formação", candidato.formacao],
                ["Descrição", candidato.descricao],
                ["Competências", candidato.competencias.join(", ")]
            ]
        }),
        curtir: async ({item: candidato}) => (await curtirCandidato(empresa.id, candidato.id, vaga.id)).match,
        mensagemDeMatch: ({item: candidato}) => `Deu match com o candidato ${candidato.id} para a vaga "${vaga.nome}"!`,
        mensagemFinal: "Não há mais candidatos com afinidade para esta vaga."
    });
}

function mostrarResumoDaVaga(vaga: Vaga): void {
    const titulo = document.getElementById("titulo-vaga");
    const resumo = document.getElementById("resumo-vaga");

    if (titulo) {
        titulo.textContent = vaga.nome;
    }

    if (resumo) {
        resumo.textContent = `${vaga.cidade} - ${vaga.estado} · Competências: ${vaga.competencias.join(", ")}`;
    }
}