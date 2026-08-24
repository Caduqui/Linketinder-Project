CREATE TABLE "candidatos" (
  "id" serial PRIMARY KEY,
  "nome" varchar(50) NOT NULL,
  "sobrenome" varchar(50) NOT NULL,
  "data_nascimento" date NOT NULL,
  "email" varchar(200) UNIQUE NOT NULL,
  "cpf" varchar(15) UNIQUE NOT NULL,
  "pais" varchar(100) NOT NULL,
  "cep" varchar(15) NOT NULL,
  "descricao" varchar(500) NOT NULL,
  "senha" varchar(50) NOT NULL,
  "formacao" varchar(100) NOT NULL
);

CREATE TABLE "competencias" (
  "id" serial PRIMARY KEY,
  "nome" varchar(50) UNIQUE NOT NULL
);

CREATE TABLE "empresas" (
  "id" serial PRIMARY KEY,
  "nome" varchar(100) NOT NULL,
  "email" varchar(200) UNIQUE NOT NULL,
  "cnpj" varchar(30) UNIQUE NOT NULL,
  "pais" varchar(100) NOT NULL,
  "cep" varchar(15) NOT NULL,
  "descricao" varchar(500) NOT NULL,
  "senha" varchar(50) NOT NULL
);

CREATE TABLE "vagas" (
  "id" serial PRIMARY KEY,
  "nome" varchar(100) NOT NULL,
  "descricao" varchar(500) NOT NULL,
  "estado" varchar(2) NOT NULL,
  "cidade" varchar(100) NOT NULL,
  "id_empresa" int NOT NULL
);

CREATE TABLE "candidato_competencia" (
  "id" serial PRIMARY KEY,
  "id_candidato" int NOT NULL,
  "id_competencia" int NOT NULL
);

CREATE TABLE "vaga_competencia" (
  "id" serial PRIMARY KEY,
  "id_vaga" int NOT NULL,
  "id_competencia" int NOT NULL
);

ALTER TABLE "vagas" ADD FOREIGN KEY ("id_empresa") REFERENCES "empresas" ("id");

ALTER TABLE "candidato_competencia" ADD FOREIGN KEY ("id_candidato") REFERENCES "candidatos" ("id");

ALTER TABLE "candidato_competencia" ADD FOREIGN KEY ("id_competencia") REFERENCES "competencias" ("id");

ALTER TABLE "vaga_competencia" ADD FOREIGN KEY ("id_vaga") REFERENCES "vagas" ("id");

ALTER TABLE "vaga_competencia" ADD FOREIGN KEY ("id_competencia") REFERENCES "competencias" ("id");

-- Inserimos as competências antes

INSERT INTO competencias (nome) VALUES ('Java'), ('Spring Framework'), ('SQL'), ('Angular'), ('Python'), ('UI/UX'), ('Figma'), ('Comunicação'), ('Soft Skills'), ('Groovy');

-- Agora os candidatos

INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao, senha, formacao) 
VALUES (
	'Ana',
	'Silva',
	'2003-04-21',
	'ana@gmail.com',
	'111.111.111-11',
	'Brasil',
	'23134-201',
	'Desenvolvedora Back-End.',
	'123456',
	'Ensino médio'
	);

INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao, senha, formacao) 
VALUES (
	'Carlos',
	'Souza',
	'1995-02-08',
	'carlos123@gmail.com',
	'222.222.222-22',
	'Brasil',
	'34142-321',
	'Desenvolvedor Front-End',
	'654321',
	'Ensino Superior Completo'
);

INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao, senha, formacao) 
VALUES (
	'Bruno',
	'Oliveira',
	'1985-03-20',
	'bruno@gmail.com',
	'333.333.333-33',
	'Brasil',
	'93241-321',
	'Desenvolvedor Full-Stack',
	'654321',
	'Ensino Superior Incompleto'
);

INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao, senha, formacao) 
VALUES (
	'Daniela',
	'Lima',
	'2007-01-15',
	'daniela@gmail.com',
	'444.444.444-44',
	'Brasil',
	'48321-831',
	'Desenvolvedora Mobile',
	'654321',
	'Ensino Médio Completo'
);

INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao, senha, formacao) 
VALUES (
	'Junior',
	'Pereira',
	'1974-06-10',
	'junior@gmail.com',
	'555.555.555-55',
	'Brasil',
	'54371-854',
	'Designer',
	'654321',
	'Ensino Superior Incompleto'
);

-- Agora as empresas

INSERT INTO empresas (nome, email, cnpj, pais, cep, descricao, senha)
VALUES (
	'NovaSolucoes',
	'empresa@novasolucoes.com',
	'11.111.111/1111-11',
	'Brasil',
	'93412-341',
	'Empresa especializada em desenvolvimento de software.',
	'123456'
);

INSERT INTO empresas (nome, email, cnpj, pais, cep, descricao, senha)
VALUES (
	'MercadoBaratao',
	'empresa@mercadobaratao.com',
	'22.222.222/2222-22',
	'Brasil',
	'32541-236',
	'Rede de supermercados.',
	'123456'
);

INSERT INTO empresas (nome, email, cnpj, pais, cep, descricao, senha)
VALUES (
	'TechVision',
	'contato@techvision.com',
	'33.333.333/3333-33',
	'Brasil',
	'79020-150',
	'Empresa especializada em desenvolvimento de sistemas web e aplicativos.',
	'123456'
);

INSERT INTO empresas (nome, email, cnpj, pais, cep, descricao, senha)
VALUES (
	'CodeMaster',
	'contato@codemaster.com',
	'44.444.444/4444-44',
	'Brasil',
	'80530-120',
	'Empresa de tecnologia focada em soluções corporativas e serviços em nuvem.',
	'123456'
);

INSERT INTO empresas (nome, email, cnpj, pais, cep, descricao, senha)
VALUES (
	'DesignFuture',
	'contato@designfuture.com',
	'55.555.555/5555-55',
	'Brasil',
	'78040-210',
	'Estúdio especializado em design de interfaces, experiência do usuário e produtos digitais.',
	'123456'
);

-- Por fim as vagas

INSERT INTO vagas (nome, descricao, estado, cidade, id_empresa)
VALUES (
	'Desenvolvedor Full-Stack',
	'Desenvolvimento e manutenção de sistemas web utilizando Java, Angular e SQL.',
	'MS',
	'Campo Grande',
	1
);

INSERT INTO vagas (nome, descricao, estado, cidade, id_empresa)
VALUES (
	'Operador de Caixa',
	'Atendimento ao cliente, operação de caixa e organização do setor.',
	'SP',
	'São Paulo',
	2
);

INSERT INTO vagas (nome, descricao, estado, cidade, id_empresa)
VALUES (
	'Desenvolvedor Backend',
	'Desenvolvimento de APIs e serviços utilizando Java, Spring Framework e SQL.',
	'MS',
	'Campo Grande',
	3
);

INSERT INTO vagas (nome, descricao, estado, cidade, id_empresa)
VALUES (
	'Engenheiro de Software',
	'Desenvolvimento e manutenção de soluções corporativas e serviços em nuvem.',
	'PR',
	'Curitiba',
	4
);

INSERT INTO vagas (nome, descricao, estado, cidade, id_empresa)
VALUES (
	'Designer UI/UX',
	'Criação de interfaces, protótipos e experiências digitais para aplicações web e mobile.',
	'MT',
	'Cuiabá',
	5
);

------- Agora é a hora de mesclar as competências com os candidatos:

--Ana: Java, Spring Framework e SQL
INSERT INTO candidato_competencia (id_candidato, id_competencia) VALUES (1, 1), (1, 2), (1, 3);

--Carlos: Java, Angular e SQL
INSERT INTO candidato_competencia (id_candidato, id_competencia) VALUES (2, 1), (2, 4), (2, 3);

--Bruno: Java, Spring Framework e Python
INSERT INTO candidato_competencia (id_candidato, id_competencia) VALUES (3, 1), (3, 2), (3, 5);

--Daniela: Python e SQL
INSERT INTO candidato_competencia (id_candidato, id_competencia) VALUES (4, 5), (4, 3);

--Junior: UI/UX e Figma
INSERT INTO candidato_competencia (id_candidato, id_competencia) VALUES (5, 6), (5, 7);

------- Agora vamos adicionar as competencias nas nossas vagas:

--Vaga Desenvolvedor Full-Stack, competecências: Java, Angular e SQL
INSERT INTO vaga_competencia (id_vaga, id_competencia) VALUES (1, 1), (1, 4), (1, 3);

--Vaga Operador de Caixa, competecências: Comunicação e Soft Skills
INSERT INTO vaga_competencia (id_vaga, id_competencia) VALUES (2, 8), (2, 9);

--Vaga Desenvolvedor Backend, competecências: Java, Spring Framework e SQL
INSERT INTO vaga_competencia (id_vaga, id_competencia) VALUES (3, 1), (3, 2), (3, 3);

--Vaga Engenheiro de Software, competecências: Java, Groovy e SQL
INSERT INTO vaga_competencia (id_vaga, id_competencia) VALUES (4, 1), (4, 10), (4, 3);

--Vaga Designer UI/UX, , competecências: UI/UX e Figma
INSERT INTO vaga_competencia (id_vaga, id_competencia) VALUES (5, 6), (5, 7);

-- nome da empresa associado com sua vaga

SELECT e.nome AS empresa, v.nome AS vaga
	FROM empresas AS e, vagas AS v
	WHERE e.id = v.id_empresa;

-- candidato e competencia

SELECT c.nome AS candidatos, comp.nome AS competencias
	FROM candidatos AS c, competencias AS comp, candidato_competencia AS cc
	WHERE c.id = cc.id_candidato
	AND comp.id = cc.id_competencia
	ORDER BY c.id;

-- vaga e competencia

SELECT v.nome AS vagas, comp.nome AS competencias
	FROM vagas AS v, competencias AS comp, vaga_competencia AS vc
	WHERE v.id = vc.id_vaga
	AND comp.id = vc.id_competencia
	ORDER BY v.id;

-- empresa junto com sua vaga e competencias

SELECT e.nome AS empresa, v.nome AS vaga, comp.nome AS competencia
	FROM empresas AS e, vagas AS v, competencias AS comp, vaga_competencia AS vc
	WHERE e.id = v.id_empresa
	AND v.id = vc.id_vaga
	AND comp.id = vc.id_competencia
	ORDER BY e.id, v.id;

-- nome do candidato que corresponde a uma vaga com base nas suas competencias

SELECT DISTINCT c.nome AS candidatos, v.nome AS vaga, e.nome AS empresas
	FROM candidatos AS c, empresas AS e, vaga_competencia AS vc, candidato_competencia AS cc, competencias AS comp, vagas AS v
	WHERE c.id = cc.id_candidato
	AND cc.id_competencia = comp.id
	AND comp.id = vc.id_competencia
	AND vc.id_vaga = v.id
	AND v.id_empresa = e.id