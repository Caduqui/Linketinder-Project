import type {Candidato, Empresa, Vaga} from "./modelos";

const CANDIDATOS_INICIAIS: Candidato[] = [
    {
        id: 1,
        nome: "Ana",
        sobrenome: "Silva",
        dataNascimento: "2003-04-21",
        email: "ana@gmail.com",
        cpf: "111.111.111-11",
        pais: "Brasil",
        cep: "23134-201",
        descricao: "Desenvolvedora back-end.",
        senha: "123456",
        formacao: "Ensino médio",
        competencias: ["Java", "Spring Framework", "SQL"]
    },
    {
        id: 2,
        nome: "Carlos",
        sobrenome: "Souza",
        dataNascimento: "1995-02-08",
        email: "carlos123@gmail.com",
        cpf: "222.222.222-22",
        pais: "Brasil",
        cep: "34142-321",
        descricao: "Desenvolvedor front-end.",
        senha: "123456",
        formacao: "Análise e Desenvolvimento de Sistemas",
        competencias: ["Java", "Angular", "SQL"]
    },
    {
        id: 3,
        nome: "Bruno",
        sobrenome: "Oliveira",
        dataNascimento: "1985-03-20",
        email: "bruno@gmail.com",
        cpf: "333.333.333-33",
        pais: "Brasil",
        cep: "93241-321",
        descricao: "Desenvolvedor full-stack.",
        senha: "123456",
        formacao: "Engenharia de Software",
        competencias: ["Java", "Spring Framework", "Python"]
    },
    {
        id: 4,
        nome: "Daniela",
        sobrenome: "Lima",
        dataNascimento: "2007-01-15",
        email: "daniela@gmail.com",
        cpf: "444.444.444-44",
        pais: "Brasil",
        cep: "48321-831",
        descricao: "Desenvolvedora mobile.",
        senha: "123456",
        formacao: "Sistemas de Informação",
        competencias: ["Python", "SQL"]
    },
    {
        id: 5,
        nome: "Junior",
        sobrenome: "Pereira",
        dataNascimento: "1974-06-10",
        email: "junior@gmail.com",
        cpf: "555.555.555-55",
        pais: "Brasil",
        cep: "54371-854",
        descricao: "Designer.",
        senha: "123456",
        formacao: "Design Digital",
        competencias: ["UI/UX", "Figma"]
    }
];

const EMPRESAS_INICIAIS: Empresa[] = [
    {
        id: 1,
        nome: "NovaSolucoes",
        email: "empresa@novasolucoes.com",
        cnpj: "11.111.111/1111-11",
        pais: "Brasil",
        cep: "93412-341",
        descricao: "Empresa especializada em desenvolvimento de software.",
        senha: "123456"
    },
    {
        id: 2,
        nome: "MercadoBaratao",
        email: "empresa@mercadobaratao.com",
        cnpj: "22.222.222/2222-22",
        pais: "Brasil",
        cep: "32541-236",
        descricao: "Rede de supermercados.",
        senha: "123456"
    },
    {
        id: 3,
        nome: "TechVision",
        email: "contato@techvision.com",
        cnpj: "33.333.333/3333-33",
        pais: "Brasil",
        cep: "79020-150",
        descricao: "Empresa especializada em desenvolvimento de sistemas web e aplicativos.",
        senha: "123456"
    },
    {
        id: 4,
        nome: "CodeMaster",
        email: "contato@codemaster.com",
        cnpj: "44.444.444/4444-44",
        pais: "Brasil",
        cep: "80530-120",
        descricao: "Empresa de tecnologia focada em soluções corporativas e serviços em nuvem.",
        senha: "123456"
    },
    {
        id: 5,
        nome: "DesignFuture",
        email: "contato@designfuture.com",
        cnpj: "55.555.555/5555-55",
        pais: "Brasil",
        cep: "78040-210",
        descricao: "Estúdio especializado em design de interfaces, experiência do usuário e produtos digitais.",
        senha: "123456"
    }
];

const VAGAS_INICIAIS: Vaga[] = [
    {
        id: 1,
        nome: "Desenvolvedor Full-Stack",
        descricao: "Desenvolvimento e manutenção de sistemas web utilizando Java, Angular e SQL.",
        estado: "MS",
        cidade: "Campo Grande",
        idEmpresa: 1,
        competencias: ["Java", "Angular", "SQL"]
    },
    {
        id: 2,
        nome: "Operador de Caixa",
        descricao: "Atendimento ao cliente, operação de caixa e organização do setor.",
        estado: "SP",
        cidade: "São Paulo",
        idEmpresa: 2,
        competencias: ["Comunicação", "Soft Skills"]
    },
    {
        id: 3,
        nome: "Desenvolvedor Backend",
        descricao: "Desenvolvimento de APIs e serviços utilizando Java, Spring Framework e SQL.",
        estado: "MS",
        cidade: "Campo Grande",
        idEmpresa: 3,
        competencias: ["Java", "Spring Framework", "SQL"]
    },
    {
        id: 4,
        nome: "Engenheiro de Software",
        descricao: "Desenvolvimento e manutenção de soluções corporativas e serviços em nuvem.",
        estado: "PR",
        cidade: "Curitiba",
        idEmpresa: 4,
        competencias: ["Java", "Groovy", "SQL"]
    },
    {
        id: 5,
        nome: "Designer UI/UX",
        descricao: "Criação de interfaces, protótipos e experiências digitais para aplicações web e mobile.",
        estado: "MT",
        cidade: "Cuiabá",
        idEmpresa: 5,
        competencias: ["UI/UX", "Figma"]
    }
];

const candidatosSalvo = localStorage.getItem("candidatos");
const empresasSalvo = localStorage.getItem("empresas");
const vagasSalvo = localStorage.getItem("vagas");

export let candidatos: Candidato[] = candidatosSalvo ? JSON.parse(candidatosSalvo) : CANDIDATOS_INICIAIS;
export let empresas: Empresa[] = empresasSalvo ? JSON.parse(empresasSalvo) : EMPRESAS_INICIAIS;
export let vagas: Vaga[] = vagasSalvo ? JSON.parse(vagasSalvo) : VAGAS_INICIAIS;

export function salvarCandidatos(): void {
    localStorage.setItem("candidatos", JSON.stringify(candidatos));
}

export function salvarEmpresas(): void {
    localStorage.setItem("empresas", JSON.stringify(empresas));
}

export function salvarVagas(): void {
    localStorage.setItem("vagas", JSON.stringify(vagas));
}