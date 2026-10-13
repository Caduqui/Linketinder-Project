import {obterUsuarioDoTipo} from "../sessao.ts";
import {listarCandidatos, listarVagasDaEmpresa} from "../api/api.ts";
import {criarCard, mostrarAviso} from "../dom.ts";
import {desenharGraficoDeCompetencias} from "../graficoCompetencias.ts";
import type {Vaga} from "../modelos.ts";

export async function carregarMinhasVagas(): Promise<void> {
    const empresa = obterUsuarioDoTipo("empresa");
    const lista = document.getElementById("lista-minhas-vagas");

    if (!empresa || !lista) {
        return;
    }

    const [vagas, candidatos] = await Promise.all([listarVagasDaEmpresa(empresa.id), listarCandidatos()]);

    if (vagas.length === 0) {
        mostrarAviso(lista, "Você ainda não cadastrou nenhuma vaga. Use \"Cadastrar vagas\" no menu.");
    } else {
        lista.replaceChildren(...vagas.map(criarItemDeVaga));
    }

    desenharGraficoDeCompetencias(candidatos);
}

function criarItemDeVaga(vaga: Vaga): HTMLLIElement {
    const item = criarCard(vaga.nome, [
        ["Localização", `${vaga.cidade} - ${vaga.estado}`],
        ["Competências", vaga.competencias.join(", ")]
    ]);
    const link = document.createElement("a");

    item.classList.add("item-vaga");
    link.className = "botao-link";
    link.href = `vagaCandidatos.html?id=${vaga.id}`;
    link.textContent = "Acessar vaga";

    item.appendChild(link);
    return item;
}