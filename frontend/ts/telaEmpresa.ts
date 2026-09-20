import {candidatos} from "./dados";

declare const Chart: any;
let chartInstance: any = null;

export function renderizarCandidatosAnonimos(): void {
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

export function atualizarGrafico(): void {
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