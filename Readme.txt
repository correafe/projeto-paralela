===========================================================================
PROJETO: SISTEMA DE GESTÃO DE VEÍCULOS EM MICROSERVIÇOS
===========================================================================

1. IDENTIFICAÇÃO
----------------
Desenvolvedor:
- Felipe de Godoi Corrêa

Disciplina: Computação Paralela e Distribuída
Orientador: Prof. Paulo Alexandre Bressan


2. DESCRIÇÃO DO PROJETO
-----------------------
Sistema distribuído para gestão de veículos (CRUD) implementado com arquitetura 
de microserviços. O sistema permite cadastrar, listar, buscar, alterar e remover 
veículos, utilizando um banco de dados relacional para persistência.

A solução é composta por:
1. Microserviço de Domínio (Veículos): Responsável pela lógica de negócio e banco de dados.
2. API Gateway: Ponto único de entrada que roteia requisições.
3. Cliente Java: Aplicação de terminal que consome a API via Gateway.
4. Frontend Web: Interface gráfica em Python (Streamlit) para interação amigável.


3. FUNCIONALIDADES
------------------
- Inserir novo veículo (Placa, Marca, Modelo, Ano, Cor, KM, Valor).
- Remover veículo por placa.
- Buscar veículo específico por placa.
- Alterar dados de um veículo existente.
- Listar todos os veículos cadastrados.
- Interface visual com tabelas e formulários (Web).


4. FERRAMENTAS E TECNOLOGIAS
----------------------------
- Linguagem: Java 17 (Backend/Cliente), Python 3 (Frontend).
- Framework: Spring Boot 3.3.5 (Web, Data JPA).
- Roteamento: Spring Cloud Gateway 2023.0.3.
- Banco de Dados: PostgreSQL 16.
- Interface Web: Streamlit.
- Build: Maven.


5. REQUISITOS MÍNIMOS
---------------------
- Processador: Dual Core 2.0GHz ou superior.
- Memória RAM: 4GB (Recomendado 8GB para rodar todos os serviços simultaneamente).
- Espaço em Disco: 500MB livres.
- Sistema Operacional: Linux (Ubuntu), Windows ou macOS.
- Softwares instalados: Java JDK 17+, Maven, Python 3+, PostgreSQL.


6. INSTRUÇÕES DE COMPILAÇÃO E EXECUÇÃO
======================================

PASSO 0: Configuração do Banco de Dados
---------------------------------------
Certifique-se de que o PostgreSQL está rodando e o banco existe:
1. Crie um banco de dados chamado 'veiculos_db'.
2. Configure o usuário 'postgres' com a senha 'Trabalho@2025' (ou altere em veiculos-service/src/main/resources/application.properties).

PASSO 1: Executar o Microserviço (Backend)
------------------------------------------
1. Acesse a pasta 'veiculos-service'.
2. Execute: ./mvnw spring-boot:run
   (Aguarde a mensagem "Started VeiculosServiceApplication")

PASSO 2: Executar o API Gateway
-------------------------------
1. Acesse a pasta 'api-gateway'.
2. Execute: ./mvnw spring-boot:run
   (Aguarde a mensagem "Netty started on port 8082")

PASSO 3: Executar o Cliente (Opção A - Terminal Java)
-----------------------------------------------------
1. Acesse a pasta 'veiculos-service/src/main/java'.
2. Compile: javac br/edu/unifal/veiculos_service/ClienteHttp.java
3. Execute: java br.edu.unifal.veiculos_service.ClienteHttp

PASSO 4: Executar o Frontend (Opção B - Interface Web)
------------------------------------------------------
1. No terminal, na pasta raiz do projeto, execute o script de automação:
   ./run_frontend.sh

   (Este script cria o ambiente virtual, instala as dependências necessárias
    automaticamente e abre o sistema no seu navegador padrão em http://localhost:8501).

===========================================================================