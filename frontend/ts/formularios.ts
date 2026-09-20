import type {Candidato, Empresa, Vaga} from "./modelos";
import {candidatos, empresas, salvarCandidatos, salvarEmpresas, salvarVagas, vagas} from "./dados";

export function configurarFormularioCandidato(): void {
    const formCandidato = document.getElementById("form-candidato") as HTMLFormElement | null;

    if (!formCandidato) {
        return;
    }

    formCandidato.addEventListener("submit", (e) => {
        e.preventDefault();
        const novoCandidato: Candidato = {
            id: candidatos.length + 1,
            nome: (document.getElementById("candidato_nome") as HTMLInputElement).value,
            sobrenome: (document.getElementById("candidato_sobrenome") as HTMLInputElement).value,
            dataNascimento: ((document.getElementById("candidato_dataNascimento") as HTMLInputElement).value),
            email: (document.getElementById("candidato_email") as HTMLInputElement).value,
            cpf: (document.getElementById("candidato_cpf") as HTMLInputElement).value,
            pais: (document.getElementById("candidato_pais") as HTMLInputElement).value,
            cep: (document.getElementById("candidato_cep") as HTMLInputElement).value,
            descricao: (document.getElementById("candidato_descricao") as HTMLInputElement).value,
            senha: (document.getElementById("candidato_senha") as HTMLInputElement).value,
            formacao: (document.getElementById("candidato_formacao") as HTMLInputElement).value,
            competencias: (document.getElementById("candidato_competencias") as HTMLInputElement).value.split(",").map(s => s.trim())
        };
        candidatos.push(novoCandidato);
        salvarCandidatos();

        localStorage.setItem("candidatoAtualId", novoCandidato.id.toString());

        formCandidato.reset();

        window.location.href = "candidato.html";
    });
}

export function configurarFormularioEmpresa(): void {
    const formEmpresa = document.getElementById("form-empresa") as HTMLFormElement | null;
    if (!formEmpresa) {
        return;
    }
    formEmpresa.addEventListener("submit", (e) => {
        e.preventDefault();
        const novaEmpresa: Empresa = {
            id: empresas.length + 1,
            nome: (document.getElementById("empresa_nome") as HTMLInputElement).value,
            email: (document.getElementById("empresa_email") as HTMLInputElement).value,
            cnpj: (document.getElementById("empresa_cnpj") as HTMLInputElement).value,
            pais: (document.getElementById("empresa_pais") as HTMLInputElement).value,
            cep: (document.getElementById("empresa_cep") as HTMLInputElement).value,
            descricao: (document.getElementById("empresa_descricao") as HTMLInputElement).value,
            senha: (document.getElementById("empresa_senha") as HTMLInputElement).value,
        };

        empresas.push(novaEmpresa);
        salvarEmpresas();
        localStorage.setItem("empresaAtualId", novaEmpresa.id.toString());
        formEmpresa.reset();
        window.location.href = "vaga.html";
    });
}

export function configurarFormularioVaga(): void {
    const formVaga = document.getElementById("form-vaga") as HTMLFormElement | null;
    if (!formVaga) {
        return;
    }
    formVaga.addEventListener("submit", (e) => {
        e.preventDefault();

        const idEmpresa = Number((document.getElementById("vaga-empresa") as HTMLSelectElement).value);

        if (!idEmpresa) {
            alert("Selecione uma empresa!")
            return;
        }

        const novaVaga: Vaga = {
            id: vagas.length + 1,
            nome: (document.getElementById("vaga_nome") as HTMLInputElement).value,
            descricao: (document.getElementById("vaga_descricao") as HTMLInputElement).value,
            estado: (document.getElementById("vaga_estado") as HTMLInputElement).value,
            cidade: (document.getElementById("vaga_cidade") as HTMLInputElement).value,
            idEmpresa: idEmpresa,
            competencias: (document.getElementById("vaga_competencias") as HTMLInputElement).value.split(",").map(s => s.trim())
        };

        vagas.push(novaVaga);
        salvarVagas();
        formVaga.reset();
        window.location.href = "empresa.html";
    });
}