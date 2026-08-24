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

let candidatos: Candidato[] = [
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

let empresas: Empresa[] = [
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

let vagas: Vaga[] = [
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

//Local Storage
const candidatosSalvo = localStorage.getItem("candidatos");
const empresasSalvo = localStorage.getItem("empresas");
const vagasSalvo = localStorage.getItem("vagas");

if (candidatosSalvo) {
    candidatos = JSON.parse(candidatosSalvo);
}

if (empresasSalvo) {
    empresas = JSON.parse(empresasSalvo);
}

if (vagasSalvo) {
    vagas = JSON.parse(vagasSalvo);
}

//Chart
declare const Chart: any;
let chartInstance: any = null;

function salvarCandidatos(): void {
    localStorage.setItem("candidatos", JSON.stringify(candidatos));
}

function salvarEmpresas(): void {
    localStorage.setItem("empresas", JSON.stringify(empresas));
}

function salvarVagas(): void {
    localStorage.setItem("vagas", JSON.stringify(vagas));
}

(document.getElementById("buttonTelaCadastro") as HTMLButtonElement).addEventListener("click", () => {
    window.location.href = 'index.html'
});

(document.getElementById("buttonTelaCandidato") as HTMLButtonElement).addEventListener("click", () => {
    window.location.href = 'candidato.html'
});

(document.getElementById("buttonTelaEmpresa") as HTMLButtonElement).addEventListener("click", () => {
    window.location.href = 'empresa.html'
});

(document.getElementById("buttonTelaVaga") as HTMLButtonElement).addEventListener("click", () => {
    window.location.href = 'vaga.html'
});


function renderizarCandidatosAnonimos(): void {
    const lista = document.getElementById("lista-candidatos")!;
    lista.innerHTML = "";

    candidatos.forEach(c => {
        const li = document.createElement("li");
        li.className = "card";
        li.innerHTML = `
      <strong>Candidato ${c.id}</strong>
      <p><strong>Formação:</strong> ${c.formacao}</p>
      <p><strong>Competências:</strong> ${c.competencias.join(", ")}</p>
    `;
        lista.appendChild(li);
    });
}

function renderizarVagasAnonimas(): void {
    const lista = document.getElementById("lista-vagas")!;
    lista.innerHTML = "";

    vagas.forEach(v => {
        const empresa = empresas.find(e => e.id === v.idEmpresa);

        const li = document.createElement("li");
        li.className = "card";
        li.innerHTML = `
            <strong>Vaga ${v.id}</strong>
            <p ><strong>Nome:</strong> ${v.nome}</p>
            <p ><strong>Empresa:</strong> ${empresa?.nome ?? "Empresa não encontrada"}</p>
            <p><strong>Descrição da Vaga:</strong> ${v.descricao}</p>
            <p><strong>Localização:</strong> ${v.cidade} - ${v.estado}</p>
            <p><strong>Competências:</strong> ${v.competencias.join(", ")}</p>
        `;
        lista.appendChild(li);
    });
}

function atualizarGrafico(): void {
    const contagemCompetencias: { [key: string]: number } = {};

    candidatos.forEach(c => {
        c.competencias.forEach(comp => {
            const formatted = comp.trim();
            contagemCompetencias[formatted] = (contagemCompetencias[formatted] || 0) + 1;
        });
    });

    const labels = Object.keys(contagemCompetencias);
    const data = Object.values(contagemCompetencias);

    const ctx = (document.getElementById("chartCompetencias") as HTMLCanvasElement).getContext("2d");

    if (chartInstance) {
        chartInstance.destroy();
    }

    chartInstance = new Chart(ctx, {
        type: "bar",
        data: {
            labels: labels,
            datasets: [{
                label: "Numéro de Candidatos por Competência",
                data: data,
                backgroundColor: "#4caf50"
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {beginAtZero: true, ticks: {stepSize: 1}}
            }
        }
    });
}

export function inicializarApp(): void {

    const formCandidato = document.getElementById("form-candidato") as HTMLFormElement | null;
    if (formCandidato) {
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
            formCandidato.reset();

            window.location.href = "candidato.html";
        });
    }

    const formEmpresa = document.getElementById("form-empresa") as HTMLFormElement | null;
    if (formEmpresa) {
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

            // Salvamos a empresa e somos direcionados para criar a vaga
            empresas.push(novaEmpresa);
            salvarEmpresas();
            localStorage.setItem("empresaAtualId", novaEmpresa.id.toString());
            formEmpresa.reset();
            window.location.href = "vaga.html";
        });
    }

    const formVaga = document.getElementById("form-vaga") as HTMLFormElement | null;
    if (formVaga) {
        formVaga.addEventListener("submit", (e) => {
            e.preventDefault();

            // pegamos o ID com o localstorage para fazer a verificacao
            const empresaAtualIdString = localStorage.getItem("empresaAtualId");

            if (!empresaAtualIdString) {
                alert("Cadastre uma empresa antes de cadastrar uma vaga.");

                window.location.href = "index.html";

                return;
            }

            // verificado transformamos em numero para usar em idEmpresa
            const empresaAtualId = Number(empresaAtualIdString);

            const novaVaga: Vaga = {
                id: vagas.length + 1,
                nome: (document.getElementById("vaga_nome") as HTMLInputElement).value,
                descricao: (document.getElementById("vaga_descricao") as HTMLInputElement).value,
                estado: (document.getElementById("vaga_estado") as HTMLInputElement).value,
                cidade: (document.getElementById("vaga_cidade") as HTMLInputElement).value,
                idEmpresa: empresaAtualId,
                competencias: (document.getElementById("vaga_competencias") as HTMLInputElement).value.split(",").map(s => s.trim())
            };

            vagas.push(novaVaga);
            salvarVagas();
            localStorage.setItem("vagaAtualId", novaVaga.id.toString());
            formVaga.reset();
            window.location.href = "empresa.html";
            // renderizarVagasAnonimas
        })
    }
}

function renderizarVagaAtual(): void {
    const vagaAtualId = Number(localStorage.getItem("vagaAtualId"));

    const vagaAtual = vagas.find(v => v.id === vagaAtualId);
    const vagaAtualContainer = document.getElementById("vaga-atual");

    if (!vagaAtualContainer || !vagaAtual) {
        return;
    }

    vagaAtualContainer.innerHTML = `
        <h3>Vaga: ${vagaAtual.nome} </h3>
        <p><strong>Descrição:</strong> ${vagaAtual.descricao}</p>
        <p><strong>Competências procuradas:</strong> ${vagaAtual.competencias.join(", ")}</p>
    `;
}

document.addEventListener("DOMContentLoaded", () => {
    inicializarApp()

    if (document.getElementById("lista-candidatos")) {
        renderizarCandidatosAnonimos();
    }

    if (document.getElementById("lista-vagas")) {
        renderizarVagasAnonimas();
    }

    if (document.getElementById("chartCompetencias")) {
        atualizarGrafico();
    }

    if (document.getElementById("vaga-atual")) {
        renderizarVagaAtual();
    }
});
