import {empresas} from "./dados";

export function carregarSeletorEmpresas(): void {

    const select = document.getElementById("vaga-empresa") as HTMLSelectElement | null;

    if (!select) {
        return;
    }

    empresas.forEach(empresa => {
        const option = document.createElement("option");

        option.value = empresa.id.toString();
        option.textContent = empresa.nome;

        select.appendChild(option);
    });
}