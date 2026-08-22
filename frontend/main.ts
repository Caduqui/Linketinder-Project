export interface Candidato {
    id: number;
    nome: string;
    email: string;
    cpf: string;
    idade: number;
    estado: string;
    cep: string;
    descricao: string;
    competencias: string[];
}

export interface Empresa {
    id: number;
    nome: string;
    email: string;
    cnpj: string;
    pais: string;
    estado: string;
    cep: string;
    descricao: string;
    competencias: string[];
}

let candidatos: Candidato[] = [
    {
        id: 1,
        nome: "Ana",
        email: "ana@gmail.com",
        cpf: "111.111.111-11",
        idade: 23,
        estado: "SP",
        cep: "23134-201",
        descricao: "Desenvolvedora back-end.",
        competencias: ["Java", "Spring Framework", "SQL"]
    },
    {
        id: 2,
        nome: "Carlos",
        email: "carlos123@gmail.com",
        cpf: "222.222.222-22",
        idade: 31,
        estado: "MS",
        cep: "34142-321",
        descricao: "Desenvolvedor front-end.",
        competencias: ["Java", "Angular", "SQL"]
    },
    {
        id: 3,
        nome: "Bruno",
        email: "bruno@gmail.com",
        cpf: "333.333.333-33",
        idade: 41,
        estado: "RJ",
        cep: "93241-321",
        descricao: "Desenvolvedor full-stack.",
        competencias: ["Java", "Spring Framework", "Python"]
    },
    {
        id: 4,
        nome: "Daniela",
        email: "daniela@gmail.com",
        cpf: "444.444.444-44",
        idade: 19,
        estado: "PR",
        cep: "48321-831",
        descricao: "Desenvolvedora mobile.",
        competencias: ["Python", "SQL"]
    },
    {
        id: 5,
        nome: "Junior",
        email: "junior@gmail.com",
        cpf: "555.555.555-55",
        idade: 52,
        estado: "MT",
        cep: "54371-854",
        descricao: "Designer.",
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
        estado: "MS",
        cep: "93412-341",
        descricao: "Aqui moldamos seu futuro!",
        competencias: ["Java", "Angular", "SQL"]
    },
    {
        id: 2,
        nome: "MercadoBaratao",
        email: "empresa@mercadobaratao.com",
        cnpj: "22.222.222/2222-22",
        pais: "Brasil",
        estado: "SP",
        cep: "32541-236",
        descricao: "Necessitamos de vaga no caixa urgente!",
        competencias: ["Comunicação", "Soft Skills"]
    }
];

//Local Storage
const candidatosSalvo = localStorage.getItem("candidatos");
const empresasSalvo = localStorage.getItem("empresas");

if (candidatosSalvo) {
    candidatos = JSON.parse(candidatosSalvo);
}

if (empresasSalvo) {
    empresas = JSON.parse(empresasSalvo);
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

(document.getElementById("buttonTelaCadastro") as HTMLButtonElement).addEventListener("click", () => {
    window.location.href = 'index.html'
});

(document.getElementById("buttonTelaCandidato") as HTMLButtonElement).addEventListener("click", ()=> {
    window.location.href = 'candidato.html'
});

(document.getElementById("buttonTelaEmpresa") as HTMLButtonElement).addEventListener("click", () => {
    window.location.href = 'empresa.html'
});


function renderizarCandidatosAnonimos(): void {
    const lista = document.getElementById("lista-candidatos")!;
    lista.innerHTML = "";

    candidatos.forEach(c => {
        const li = document.createElement("li");
        li.className = "card";
        li.innerHTML = `
      <strong>Candidato ${c.id}</strong>
      <p ><strong>Nome:</strong> <span class="blur-nome">${c.nome}</span> </p>
      <p><strong>E-mail:</strong> ${c.email}</p>
      <p><strong>CPF:</strong> ${c.cpf}</p>
      <p><strong>Idade:</strong> ${c.idade} anos</p>
      <p><strong>Localização:</strong> ${c.estado} (CEP: ${c.cep})</p>
      <p><strong>Descrição:</strong> ${c.descricao}</p>
      <p><strong>Competências:</strong> ${c.competencias.join(", ")}</p>
    `;
        lista.appendChild(li);
    });
}

function renderizarEmpresasAnonimas(): void {
    const lista = document.getElementById("lista-empresas")!;
    lista.innerHTML = "";

    empresas.forEach(e => {
        const li = document.createElement("li");
        li.className = "card";
        li.innerHTML = `
      <strong>Vaga ${e.id}</strong>
      <p ><strong>Nome: </strong> <span class="blur-nome">${e.nome}</span> </p>
      <p><strong>E-mail:</strong> ${e.email}</p>
      <p><strong>CNPJ:</strong> ${e.cnpj}</p>
      <p><strong>Localização:</strong> ${e.estado}, ${e.pais} (CEP: ${e.cep})</p>
      <p><strong>Descrição da Vaga:</strong> ${e.descricao}</p>
      <p><strong>Requisitos / Competências:</strong> ${e.competencias.join(", ")}</p>
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
                email: (document.getElementById("candidato_email") as HTMLInputElement).value,
                cpf: (document.getElementById("candidato_cpf") as HTMLInputElement).value,
                idade: Number((document.getElementById("candidato_idade") as HTMLInputElement).value),
                estado: (document.getElementById("candidato_estado") as HTMLInputElement).value,
                cep: (document.getElementById("candidato_cep") as HTMLInputElement).value,
                descricao: (document.getElementById("candidato_descricao") as HTMLInputElement).value,
                competencias: (document.getElementById("candidato_competencias") as HTMLInputElement).value.split(",").map(s => s.trim())
            };
            candidatos.push(novoCandidato);
            salvarCandidatos();
            formCandidato.reset();
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
                estado: (document.getElementById("empresa_estado") as HTMLInputElement).value,
                cep: (document.getElementById("empresa_cep") as HTMLInputElement).value,
                descricao: (document.getElementById("empresa_descricao") as HTMLInputElement).value,
                competencias: (document.getElementById("empresa_competencias") as HTMLInputElement).value.split(",").map(s => s.trim())
            };
            empresas.push(novaEmpresa);
            salvarEmpresas();
            formEmpresa.reset();
        });
    }
}

document.addEventListener("DOMContentLoaded", () => {
    inicializarApp()

    if (document.getElementById("lista-candidatos")) {
        renderizarCandidatosAnonimos();
    }

    if (document.getElementById("lista-empresas")) {
        renderizarEmpresasAnonimas();
    }

    if (document.getElementById("chartCompetencias")) {
        atualizarGrafico();
    }
});
