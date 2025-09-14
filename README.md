# Sistema de CRM para TCC

Um sistema de CRM (Customer Relationship Management) desenvolvido em Java utilizando Spring Boot. O projeto inclui cadastro e login de usuários com autenticação segura via JWT, recuperação de senha por e-mail, e uma API REST para gerenciamento de funis de venda, contatos (pessoas) e negócios. Este projeto é focado em boas práticas de desenvolvimento, arquitetura de software e segurança para aplicações web.

---

## Requisitos do Sistema de CRM

### Requisitos Funcionais (RF)

- **RF01 - Cadastro de Usuário:** O sistema deve permitir o cadastro de novos usuários, solicitando nome, e-mail e senha.
- **RF02 - Login de Usuário:** O sistema deve permitir que usuários façam login utilizando e-mail e senha, recebendo um token de autenticação (JWT).
- **RF03 - Validação de Dados:** O sistema deve validar os dados inseridos nos formulários de cadastro e login (ex: formato de e-mail, senha obrigatória).
- **RF04 - Armazenamento Seguro de Senhas:** O sistema deve armazenar as senhas dos usuários de forma criptografada (hash).
- **RF05 - E-mail Único:** O sistema deve impedir o cadastro de usuários com e-mails duplicados.
- **RF06 - Proteção de Rotas:** O sistema deve proteger rotas, permitindo acesso apenas a usuários autenticados e autorizados.
- **RF07 - API REST:** O sistema deve disponibilizar endpoints REST para todas as suas funcionalidades.
- **RF08 - Recuperação de Senha:** O sistema deve permitir que o usuário solicite a redefinição de senha, enviando um e-mail com um link seguro para recuperação.
- **RF09 - Cadastro de Funil de Vendas:** O sistema deve permitir que um usuário autenticado crie um ou mais funis de venda.
- **RF10 - Gestão de Funis:** O sistema deve permitir que o usuário liste, visualize, atualize e delete seus próprios funis.
- **RF11 - Cadastro de Pessoas (Contatos):** O sistema deve permitir que um usuário cadastre pessoas (contatos), associando-as à sua conta.
- **RF12 - Gestão de Pessoas:** O sistema deve permitir que o usuário liste, visualize, atualize e delete seus próprios contatos.
- **RF13 - Cadastro de Negócios:** O sistema deve permitir que o usuário crie um negócio, associando-o a um funil e a uma pessoa (contato).
- **RF14 - Gestão de Negócios:** O sistema deve permitir que o usuário liste, visualize, atualize (ex: mudar de etapa no funil) e delete seus próprios negócios.
- **RF15 - Perfis de Usuário:** O sistema deve suportar diferentes perfis de usuário (ex: Gerente, Vendedor) com diferentes níveis de permissão.

### Requisitos Não Funcionais (RNF)

- **RNF01 - Segurança:** O sistema deve garantir a segurança dos dados, utilizando HTTPS, JWT para autenticação e boas práticas de desenvolvimento seguro.
- **RNF02 - Performance:** O sistema deve responder às requisições em tempo hábil, mesmo com um volume crescente de dados.
- **RNF03 - Escalabilidade:** A arquitetura deve permitir a fácil adição de novos recursos e módulos no futuro.
- **RNF04 - Integridade dos Dados:** O sistema deve usar chaves estrangeiras e validações para garantir a consistência dos dados no banco.
- **RNF05 - Documentação da API:** Os endpoints da API devem ser documentados (ex: via Swagger/OpenAPI) para facilitar o consumo por um front-end.
- **RNF06 - Auditoria:** Ações críticas (login, falha de login, criação/deleção de dados) devem ser registradas em logs para fins de auditoria.
- **RNF07 - Controle de Acesso (RBAC):** O sistema deve implementar controle de acesso baseado em perfis (Role-Based Access Control).
- **RNF08 - Proteção contra Vulnerabilidades:** O sistema deve ser protegido contra vulnerabilidades comuns como SQL Injection (usando JPA), XSS e CSRF (desabilitado para API state-less, com segurança garantida pelo JWT).
- **RNF09 - Validação de Entrada:** Todas as entradas de dados devem ser validadas no back-end para garantir a integridade.

---

## Estrutura do Banco de Dados

O sistema utiliza um banco de dados relacional (MySQL/MariaDB) para armazenar as informações. As chaves primárias e estrangeiras são do tipo BIGINT para garantir escalabilidade.

### Tabelas Principais

#### **users**
Armazena os dados dos usuários cadastrados.

| Campo           | Tipo          | Descrição                                               |
|-----------------|---------------|---------------------------------------------------------|
| Id              | BIGINT (PK)   | Identificador único do usuário                          |
| Nome            | VARCHAR(100)  | Nome do usuário                                         |
| Email           | VARCHAR(100)  | E-mail de login (único)                                 |
| Senha           | VARCHAR(255)  | Senha criptografada                                     |
| Ativo           | INT           | Status da conta (1=ativo, 0=inativo)                    |
| PerfilUsuario   | BIGINT (FK)   | Referência ao Id da tabela roles                        |
| SenhaProvisoria | INT           | Flag (1=sim, 0=não)                                     |


#### **roles**
Define os perfis de usuário do sistema.

| Campo     | Tipo          | Descrição                                  |
|-----------|---------------|---------------------------------------------|
| Id        | BIGINT (PK)   | Identificador único do perfil               |
| Descricao | VARCHAR(255)  | Descrição do perfil (ex: ADMINISTRADOR)     |


#### **user**
Armazena informações de autenticação e perfil dos usuários.

| Campo           | Tipo           | Descrição                                           |
|-----------------|----------------|-----------------------------------------------------|
| id              | BIGINT (PK)    | Identificador único do usuário                      |
| perfil_usuario  | INT            | Perfil do usuário (relacionamento com roles)        |
| ativo           | BIT(1)         | Status da conta (1=ativo, 0=inativo)                |
| email           | VARCHAR(255)   | E-mail do usuário                                   |
| nome            | VARCHAR(255)   | Nome do usuário                                     |
| senha           | VARCHAR(255)   | Senha criptografada                                 |


#### **organizacoes**
Armazena os dados das organizações cadastradas.

| Campo     | Tipo           | Descrição                                                   |
|-----------|----------------|-------------------------------------------------------------|
| id        | BIGINT (PK)    | Identificador único da organização                          |
| nome      | VARCHAR(255)   | Nome da organização                                         |
| cnpj      | VARCHAR(20)    | CNPJ da organização (único)                                 |
| telefone  | VARCHAR(20)    | Telefone para contato                                       |
| email     | VARCHAR(100)   | E-mail para contato                                         |
| endereco  | VARCHAR(255)   | Endereço da organização                                     |
| user_id   | BIGINT (FK)    | Referência ao Id do usuário que cadastrou a organização     |


#### **pessoas**
Armazena os dados das pessoas físicas cadastradas.

| Campo              | Tipo           | Descrição                                                |
|--------------------|----------------|----------------------------------------------------------|
| id                 | BIGINT (PK)    | Identificador único da pessoa                            |
| nome               | VARCHAR(255)   | Nome completo da pessoa                                  |
| cpf                | VARCHAR(255)   | CPF da pessoa                                            |
| rg                 | VARCHAR(255)   | RG da pessoa                                             |
| data_de_nascimento | DATE           | Data de nascimento                                       |
| email              | VARCHAR(255)   | E-mail da pessoa (único)                                 |
| telefone           | VARCHAR(255)   | Telefone para contato                                    |
| user_id            | BIGINT (FK)    | Referência ao Id do usuário responsável                  |
| negocio_id         | BIGINT (FK)    | Referência ao Id do negócio relacionado                  |


#### **funis**
Armazena os funis de vendas do sistema.

| Campo    | Tipo           | Descrição                                         |
|----------|----------------|---------------------------------------------------|
| id       | BIGINT (PK)    | Identificador único do funil                      |
| nome     | VARCHAR(255)   | Nome do funil                                     |
| user_id  | BIGINT (FK)    | Referência ao Id do usuário que criou o funil     |


#### **etapa**
Armazena as etapas pertencentes a um funil de vendas.

| Campo     | Tipo           | Descrição                                         |
|-----------|----------------|---------------------------------------------------|
| Id        | BIGINT (PK)    | Identificador único da etapa                      |
| Nome      | VARCHAR(255)   | Nome da etapa                                     |
| Funil_id  | BIGINT (FK)    | Referência ao Id do funil                         |


#### **negocios**
Armazena os negócios/operações do CRM.

| Campo              | Tipo            | Descrição                                                |
|--------------------|-----------------|----------------------------------------------------------|
| id                 | BIGINT (PK)     | Identificador único do negócio                           |
| titulo             | VARCHAR(255)    | Título do negócio                                        |
| pipeline_stage     | INT             | Etapa atual no funil                                     |
| id_organizacao     | INT             | Referência à organização (deprecado, usar id_ornizacao)  |
| funil_id           | BIGINT (FK)     | Referência ao Id do funil                                |
| pessoa_id          | BIGINT (FK)     | Referência ao Id da pessoa                               |
| id_ornizacao       | INT             | Referência à organização (possível erro de digitação)    |
| data_de_fechamento | VARCHAR(255)    | Data prevista para fechamento                            |
| valor              | DECIMAL(10,2)   | Valor do negócio                                         |


#### **audit_logs**
Armazena o histórico de ações realizadas por usuários.

| Campo      | Tipo          | Descrição                                              |
|------------|---------------|--------------------------------------------------------|
| id         | BIGINT (PK)   | Identificador único do registro de auditoria           |
| acao       | VARCHAR(255)  | Ação realizada (ex: criação, edição, exclusão)         |
| data       | DATE          | Data da ação                                           |
| detalhes   | VARCHAR(255)  | Detalhes adicionais sobre a ação                       |
| usuario_id | BIGINT (FK)   | Referência ao Id do usuário responsável pela ação      |


#### **password_reset_tokens**
Armazena os tokens de redefinição de senha.

| Campo           | Tipo           | Descrição                                               |
|-----------------|----------------|---------------------------------------------------------|
| id              | BIGINT (PK)    | Identificador único do token                            |
| token           | VARCHAR(255)   | Token de redefinição de senha (único)                   |
| expiration_date | DATETIME       | Data e hora de expiração do token                       |
| used            | TINYINT(1)     | Indica se o token já foi utilizado (1=sim, 0=não)       |
| user_id         | BIGINT (FK)    | Referência ao Id do usuário                             |
---

## Como rodar o projeto

### Pré-requisitos

- Java 17+
- Maven
- MySQL ou MariaDB

### Back-end

1. Configure as credenciais do seu banco de dados no arquivo `src/main/resources/application.properties`.
2. Execute o projeto com o seguinte comando na raiz do projeto:

   ```bash
   mvn spring-boot:run
   ```

3. A API estará disponível em [http://localhost:8080](http://localhost:8080).

---

## Licença

Este projeto é destinado exclusivamente para fins acadêmicos e de aprendizado.
