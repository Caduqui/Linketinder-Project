package org.example

/**
 *
 * @author Guilherme Lima Conte
 */

class Main {

    static List<Candidato> candidatos = []
    static List<Empresa> empresas = []
    static List<Vaga> vagas = []
    static Scanner scanner = new Scanner(System.in)
    static Cadastro cadastro

    static void main(String[] args) {
        carregarDados()
        cadastro = new Cadastro(candidatos, empresas, vagas, new ScannerLeitorEntrada(scanner))
        menu()
    }

    static void carregarDados() {
        candidatos << new Candidato(
                "Ana",
                "Silva",
                "2003-04-21",
                "ana@gmail.com",
                "111.111.111-11",
                "Brasil",
                "23134-201",
                "Desenvolvedora back-end.",
                "123456",
                "Ensino médio",
                ["Java", "Spring Framework", "SQL"]
        )

        candidatos << new Candidato(
                "Carlos",
                "Souza",
                "1995-02-08",
                "carlos123@gmail.com",
                "222.222.222-22",
                "Brasil",
                "34142-321",
                "Desenvolvedor front-end.",
                "123456",
                "Análise e Desenvolvimento de Sistemas",
                ["Java", "Angular", "SQL"]
        )

        candidatos << new Candidato(
                "Bruno",
                "Oliveira",
                "1985-03-20",
                "bruno@gmail.com",
                "333.333.333-33",
                "Brasil",
                "93241-321",
                "Desenvolvedor full-stack.",
                "123456",
                "Engenharia de Software",
                ["Java", "Spring Framework", "Python"]
        )

        candidatos << new Candidato(
                "Daniela",
                "Lima",
                "2007-01-15",
                "daniela@gmail.com",
                "444.444.444-44",
                "Brasil",
                "48321-831",
                "Desenvolvedora mobile.",
                "123456",
                "Sistemas de Informação",
                ["Python", "SQL"]
        )

        candidatos << new Candidato(
                "Junior",
                "Pereira",
                "1974-06-10",
                "junior@gmail.com",
                "555.555.555-55",
                "Brasil",
                "54371-854",
                "Designer.",
                "654321",
                "Ensino Superior Incompleto",
                ["UI/UX", "Figma"]
        )

        Empresa novaSolucoes = new Empresa(
                "NovaSolucoes",
                "empresa@novasolucoes.com",
                "11.111.111/1111-11",
                "Brasil",
                "93412-341",
                "Empresa especializada em desenvolvimento de software.",
                "123456"
        )

        Empresa mercadoBaratao = new Empresa(
                "MercadoBaratao",
                "empresa@mercadobaratao.com",
                "22.222.222/2222-22",
                "Brasil",
                "32541-236",
                "Rede de supermercados.",
                "123456"
        )

        Empresa techVision = new Empresa(
                "TechVision",
                "contato@techvision.com",
                "33.333.333/3333-33",
                "Brasil",
                "79020-150",
                "Empresa especializada em desenvolvimento de sistemas web e aplicativos.",
                "123456"
        )

        Empresa codeMaster = new Empresa(
                "CodeMaster",
                "contato@codemaster.com",
                "44.444.444/4444-44",
                "Brasil",
                "80530-120",
                "Empresa de tecnologia focada em soluções corporativas e serviços em nuvem.",
                "123456"
        )

        Empresa designFuture = new Empresa(
                "DesignFuture",
                "contato@designfuture.com",
                "55.555.555/5555-55",
                "Brasil",
                "78040-210",
                "Estúdio especializado em design de interfaces, experiência do usuário e produtos digitais.",
                "123456"
        )

        empresas << novaSolucoes
        empresas << mercadoBaratao
        empresas << techVision
        empresas << codeMaster
        empresas << designFuture

        vagas << new Vaga(
                "Desenvolvedor Full-Stack",
                "Desenvolvimento e manutenção de sistemas web utilizando Java, Angular e SQL.",
                "MS",
                "Campo Grande",
                novaSolucoes,
                ["Java", "Angular", "SQL"]
        )

        vagas << new Vaga(
                "Operador de Caixa",
                "Atendimento ao cliente, operação de caixa e organização do setor.",
                "SP",
                "São Paulo",
                mercadoBaratao,
                ["Comunicação", "Soft Skills"]
        )

        vagas << new Vaga(
                "Desenvolvedor Backend",
                "Desenvolvimento de APIs e serviços utilizando Java, Spring Framework e SQL.",
                "MS",
                "Campo Grande",
                techVision,
                ["Java", "Spring Framework", "SQL"]
        )

        vagas << new Vaga(
                "Engenheiro de Software",
                "Desenvolvimento e manutenção de soluções corporativas e serviços em nuvem.",
                "PR",
                "Curitiba",
                codeMaster,
                ["Java", "Groovy", "SQL"]
        )

        vagas << new Vaga(
                "Designer UI/UX",
                "Criação de interfaces, protótipos e experiências digitais para aplicações web e mobile.",
                "MT",
                "Cuiabá",
                designFuture,
                ["UI/UX", "Figma"]
        )
    }


    static void menu() {
        while (true) {
            println "\n1- Listar candidatos"
            println "2- Listar empresas"
            println "3- Listar vagas"
            println "4- Cadastrar candidato"
            println "5- Cadastrar empresa"
            println("0- Sair")
            print "Escolha sua opção: "

            switch (scanner.nextLine().trim()) {
                case "1":
                    candidatos.each {
                        it.exibirDados()
                    }
                    break
                case "2":
                    empresas.each {
                        it.exibirDados()
                    }
                    break
                case "3":
                    vagas.each {
                        it.exibirDados()
                    }
                    break
                case "4":
                    cadastro.cadastrarCandidato()
                    println "Candidato cadastrado com sucesso!"
                    break
                case "5":
                    Empresa novaEmpresa = cadastro.cadastrarEmpresa()
                    println "Empresa cadastrada com sucesso!"
                    cadastro.cadastrarVaga(novaEmpresa)
                    println("Vaga cadastrada com sucesso")
                    break
                case "0":
                    return
                default:
                    println "Opção inválida."
            }
        }
    }
}

