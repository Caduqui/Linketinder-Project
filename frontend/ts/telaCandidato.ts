import type {Candidato, Vaga} from "./modelos";
import {candidatos, vagas} from "./dados";

export function calcularAfinidade(candidato: Candidato, vaga: Vaga): number {
    if (vaga.competencias.length === 0) {
        return 0;
    }

    const competenciasCandidato = candidato.competencias.map(competencia => competencia.trim().toLowerCase());
    const competenciasVaga = vaga.competencias.map(competencia => competencia.trim().toLowerCase());
    const competenciasEmComum = competenciasVaga.filter(competencia => competenciasCandidato.includes(competencia));

    return Math.round((competenciasEmComum.length / competenciasVaga.length) * 100)
}

export function renderizarVagasAnonimas(): void {
    const lista = document.getElementById("lista-vagas")!;
    lista.innerHTML = "";

    const candidatoAtualIdString = localStorage.getItem("candidatoAtualId");

    if (!candidatoAtualIdString) {
        lista.innerHTML = `
            <li class="card"> 
                Nenhum candidato selecionado.
            </li>
        `;
        return;
    }

    const candidatoAtualId = Number(candidatoAtualIdString);
    const candidatoAtual = candidatos.find(candidato => candidato.id === candidatoAtualId);

    if (!candidatoAtual) {
        lista.innerHTML = `
            <li class="card">
                Candidato não encontrado.
            </li>
        `;
        return;
    }

    vagas.forEach(vaga => {
        const afinidade = calcularAfinidade(candidatoAtual, vaga);
        const li = document.createElement("li");

        li.className = "card";
        li.innerHTML = `
            <strong>Vaga ${vaga.id}</strong>
            <p ><strong>Nome:</strong> ${vaga.nome}</p>
            <p><strong>Descrição da Vaga:</strong> ${vaga.descricao}</p>
            <p><strong>Localização:</strong> ${vaga.cidade} - ${vaga.estado}</p>
            <p><strong>Competências:</strong> ${vaga.competencias.join(", ")}</p>
            <p><strong>Índice de afinidade:</strong> ${afinidade}%</p>
        `;
        lista.appendChild(li);
    });
}

export function carregarSeletorCandidatos(): void {
    const select = document.getElementById("selecionar-candidato") as HTMLSelectElement | null;

    if (!select) {
        return;
    }

    candidatos.forEach(candidato => {
        const option = document.createElement("option");

        option.value = candidato.id.toString();
        option.textContent = `${candidato.nome} ${candidato.sobrenome}`;

        select.appendChild(option);
    });

    const candidatoAtualId = localStorage.getItem("candidatoAtualId");

    if (candidatoAtualId) {
        select.value = candidatoAtualId;
    }

    select.addEventListener("change", () => {
        if (!select.value) {
            localStorage.removeItem("candidatoAtualId");
        }

        localStorage.setItem("candidatoAtualId", select.value);

        renderizarVagasAnonimas();
    });

}