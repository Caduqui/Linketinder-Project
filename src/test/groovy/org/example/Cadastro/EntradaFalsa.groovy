package org.example.Cadastro

import org.example.LeitorEntrada

class EntradaFalsa {

    static EntradaDados comRespostas(Map<String, String> respostasPorPergunta) {
        LeitorEntrada leitor = [lerLinha: { String pergunta -> respostasPorPergunta[pergunta] }] as LeitorEntrada
        return new EntradaDados(leitor)
    }

}
