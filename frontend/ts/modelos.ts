/**
 *
 * @author Guilherme Lima Conte
 */

export type TipoUsuario = "candidato" | "empresa";

export interface Candidato {
    id: number;
    nome: string;
    sobrenome: string;
    dataNascimento: string;
    email: string;
    cpf: string;
    pais: string;
    cep: string;
    descricao: string;
    formacao: string;
    competencias: string[];
}

export type NovoCandidato = Omit<Candidato, "id" | "competencias"> & {senha:string};

export interface Empresa {
    id: number;
    nome: string;
    email: string;
    cnpj: string;
    pais: string;
    cep: string;
    descricao: string;
}

export type NovaEmpresa = Omit<Empresa, "id"> & {senha:string};

export interface Vaga {
    id: number;
    nome: string;
    descricao: string;
    estado: string;
    cidade: string;
    empresa: {id: number; nome: string};
    competencias: string[];
}

export interface NovaVaga {
    nome: string;
    descricao: string;
    estado: string;
    cidade: string;
    idEmpresa: number;
    competencias: string[];
}

export interface Competencia {
    id: number;
    nome: string;
}

export interface Credenciais {
    tipo: TipoUsuario;
    email: string;
    senha: string;
}

export interface UsuarioLogado {
    tipo: TipoUsuario;
    id: number;
    nome: string;
}

export interface ResultadoCurtida {
    match: boolean;
}