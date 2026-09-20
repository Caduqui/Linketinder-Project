import {configurarFormularioCandidato, configurarFormularioEmpresa, configurarFormularioVaga} from "./formularios";
import {carregarSeletorCandidatos, renderizarVagasAnonimas} from "./telaCandidato";
import {carregarSeletorEmpresas} from "./telaVaga";
import {atualizarGrafico, renderizarCandidatosAnonimos} from "./telaEmpresa";

const PAGINA_POR_BOTAO: { [idBotao: string]: string} = {
    buttonTelaCadastro: "index.html",
    buttonTelaCandidato: "candidato.html",
    buttonTelaEmpresa: "empresa.html",
    buttonTelaVaga: "vaga.html"
};

function configurarNavegacao(): void {
    Object.keys(PAGINA_POR_BOTAO).forEach(idBotao => {
        const botao = document.getElementById(idBotao) as HTMLButtonElement | null;

        botao?.addEventListener("click", () => {
            window.location.href = PAGINA_POR_BOTAO[idBotao];
        });
    });
}

export function inicializarApp(): void {

    configurarNavegacao();
    configurarFormularioCandidato();
    configurarFormularioEmpresa();
    configurarFormularioVaga();
}

document.addEventListener("DOMContentLoaded", () => {
    inicializarApp()

    if (document.getElementById("selecionar-candidato")) {
        carregarSeletorCandidatos();
    }

    if (document.getElementById("vaga-empresa")) {
        carregarSeletorEmpresas();
    }

    if (document.getElementById("lista-candidatos")) {
        renderizarCandidatosAnonimos();
    }

    if (document.getElementById("lista-vagas")) {
        renderizarVagasAnonimas();
    }

    if (document.getElementById("chartCompetencias")) {
        atualizarGrafico();
    }

});
