package org.example

/**
 *
 * @author Guilherme Lima Conte
 */

class Main {

    static List<Candidato> candidatos = []
    static List<Empresa> empresas = []
    static Scanner scanner = new Scanner(System.in)

    static void main(String[] args) {
        carregarDados()
        menu()
    }

    static void carregarDados() {
        candidatos << new Candidato(
                "Ana",
                "ana@gmail.com",
                "111.111.111-11",
                23,
                "SP",
                "23134-201",
                "Desenvolvedora back-end.",
                ["Java", "Spring Framework", "SQL"]
        )
        candidatos << new Candidato(
                "Carlos",
                "carlos123@gmail.com",
                "222.222.222-22",
                31,
                "MS",
                "34142-321",
                "Desenvolvedor front-end.",
                ["Java", "Spring Framework", "SQL"]
        )
        candidatos << new Candidato(
                "Bruno",
                "bruno@gmail.com",
                "333.333.333-33",
                41,
                "RJ",
                "93241-321",
                "Desenvolvedor full-stack.",
                ["Java", "Spring Framework", "SQL"]
        )
        candidatos << new Candidato(
                "Daniela",
                "daniela@gmail.com",
                "444.444.444-44",
                19,
                "PR",
                "48321-831",
                "Desenvolvedora mobile.",
                ["Java", "Spring Framework", "SQL"]
        )
        candidatos << new Candidato(
                "Junior",
                "junior@gmail.com",
                "555.555.555-55",
                52,
                "MT",
                "54371-854",
                "Designer.",
                ["Java", "Spring Framework", "SQL"]
        )

        empresas << new Empresa(
                "NovaSolucoes",
                "empresa@novasolucoes.com",
                "11.111.111/1111-11",
                "Brasil",
                "MS",
                "93412-341",
                "Aqui moldamos seu futuro, venha fazer parte de novas Soluçõess!",
                ["Java", "Angular", "SQL"]
        )

        empresas << new Empresa(
                "MercadoBaratao",
                "empresa@mercadobaratao.com",
                "22.222.222/2222-22",
                "Brasil",
                "SP",
                "32541-236",
                "Necessitamos de vaga no caixa urgente!",
                ["Comunicação", "Soft Skills"]
        )

        empresas << new Empresa(
                "NelsonAdvocacia",
                "empresa@nelsonadvocacia.com",
                "33.333.333/3333-33",
                "Brasil",
                "PA",
                "93874-612",
                "Venha ser um advogado de sucesso com a gente!",
                ["Direito", "Iniciativa"]
        )

        empresas << new Empresa(
                "TonhãoAutoPecas",
                "empresa@tonhaoautopecas.com",
                "44.444.444/4444-44",
                "Brasil",
                "AC",
                "54361-296",
                "Tonhão auto pecas recrutando guerreiros que queiram entrar nessa empreitada",
                ["Vontade", "Esforçado"]
        )

        empresas << new Empresa(
                "AcademiaSmart",
                "empresa@academiasmart.com",
                "55.555.555/5555-55",
                "Brasil",
                "MS",
                "75421-847",
                "Nós da rede smart buscamos novos estagiários para fazer parte da nossa equipe!",
                ["Estágio", "Educação Física"]
        )
    }

    static void menu() {
        boolean e = true
        while (e) {
            println "\n1- Listar candidatos"
            println "2- Listar empresas"
            println "3- Cadastrar candidato"
            println "4- Cadastrar empresa"
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
                    cadastrarCandidato()
                    break
                case "4":
                    cadastrarEmpresa()
                    break
                case "0":
                    e = false
                    break;
                default:
                    println "Opção inválida."

            }
        }
    }

    static void cadastrarCandidato() {
        print "Nome: "
        def nome = scanner.nextLine()

        print "Email: "
        def email = scanner.nextLine()

        print "CPF: "
        def cpf = scanner.nextLine()

        print "Idade: "
        def idade = scanner.nextLine().toInteger()

        print "Estado: "
        def estado = scanner.nextLine()

        print "CEP: "
        def cep = scanner.nextLine()

        print "Descrição: "
        def descricao = scanner.nextLine()

        print "Competências: "
        def competencias = scanner.nextLine().split(",").collect { it.trim() }

        candidatos << new Candidato(nome, email, cpf, idade, estado, cep, descricao, competencias)

        println "Candidato cadastrado!"
    }

    static void cadastrarEmpresa() {
        print "Nome: "
        def nome = scanner.nextLine()

        print "Email: "
        def email = scanner.nextLine()

        print "CNPJ: "
        def cnpj = scanner.nextLine()

        print "País: "
        def pais = scanner.nextLine()

        print "Estado: "
        def estado = scanner.nextLine()

        print "CEP: "
        def cep = scanner.nextLine()

        print "Descrição: "
        def descricao = scanner.nextLine()

        print "Competências: "
        def competencias = scanner.nextLine().split(",").collect { it.trim() }

        empresas << new Empresa(nome, email, cnpj, pais, estado, cep, descricao, competencias)

        println "Empresa cadastrada!"
    }
}

