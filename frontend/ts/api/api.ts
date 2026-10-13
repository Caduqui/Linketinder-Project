import type {
    Candidato,
    Competencia,
    Credenciais,
    Empresa,
    NovaEmpresa,
    NovaVaga,
    NovoCandidato, ResultadoCurtida, UsuarioLogado,
    Vaga
} from "../modelos.ts";
import {http} from "./http.ts";

export function cadastrarCandidato(candidato: NovoCandidato): Promise<Candidato> {
    return http.post<Candidato>("/candidatos", candidato);
}

export function buscarCandidato(id: number): Promise<Candidato> {
    return http.get<Candidato>(`/candidatos/${id}`);
}

export function listarCandidatos(): Promise<Candidato[]> {
    return http.get<Candidato[]>("/candidatos");
}

export function cadastrarEmpresa(empresa: NovaEmpresa): Promise<Empresa> {
    return http.post<Empresa>("/empresas", empresa);
}

export function listarEmpresas(): Promise<Empresa[]> {
    return http.get<Empresa[]>("/empresas");
}

export function cadastrarVaga(vaga: NovaVaga): Promise<Vaga> {
    return http.post<Vaga>("/vagas", vaga);
}

export function listarVagas(): Promise<Vaga[]> {
    return http.get<Vaga[]>("/vagas");
}

export function listarVagasDaEmpresa(idEmpresa: number): Promise<Vaga[]> {
    return http.get<Vaga[]>(`/vagas?idEmpresa=${idEmpresa}`);
}

export function listarCompetencias(): Promise<Competencia[]> {
    return http.get<Competencia[]>("/competencias");
}

export function inserirCompetencias(idCandidato: number, competencias: string[]): Promise<Candidato> {
    return http.post<Candidato>("/competencias", {idCandidato, competencias});
}

export function fazerLogin(credenciais: Credenciais): Promise<UsuarioLogado> {
    return http.post<UsuarioLogado>("/login", credenciais);
}

export function curtirVaga(idCandidato: number, idVaga: number): Promise<ResultadoCurtida> {
    return http.post<ResultadoCurtida>("/curtidas", {tipo: "candidato", idCandidato, idVaga});
}

export function curtirCandidato(idEmpresa: number, idCandidato: number, idVaga: number): Promise<ResultadoCurtida> {
    return http.post<ResultadoCurtida>("/curtidas", {tipo: "empresa", idEmpresa, idCandidato, idVaga});
}

