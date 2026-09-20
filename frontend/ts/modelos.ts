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
    senha: string;
    formacao: string;
    competencias: string[];
}

export interface Empresa {
    id: number;
    nome: string;
    email: string;
    cnpj: string;
    pais: string;
    cep: string;
    descricao: string;
    senha: string;
}

export interface Vaga {
    id: number;
    nome: string;
    descricao: string;
    estado: string;
    cidade: string;
    idEmpresa: number;
    competencias: string[];
}