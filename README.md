# Sistema-para-TCC

Sistema de cadastro, login e controle de estoque desenvolvido em Java utilizando Spring Boot, com autenticação segura, recuperação de senha via e-mail e API REST. Projeto voltado para estudos de segurança, boas práticas em aplicações web, e gestão de estoque com controle de permissões por perfil de usuário.

---

## Requisitos do Sistema de Cadastro, Login e Controle de Estoque

### Requisitos Funcionais (RF)

- **RF01 - Cadastro de Usuário:**  
  O sistema deve permitir o cadastro de novos usuários, solicitando nome, e-mail e senha.

- **RF02 - Login de Usuário:**  
  O sistema deve permitir que usuários façam login utilizando e-mail e senha.

- **RF03 - Validação de Dados:**  
  O sistema deve validar os dados inseridos nos formulários de cadastro e login (ex: formato de e-mail, senha obrigatória).

- **RF04 - Armazenamento Seguro de Senhas:**  
  O sistema deve armazenar as senhas dos usuários de forma criptografada.

- **RF05 - Consulta de Usuário Logado:**  
  O sistema deve permitir a consulta dos dados do usuário atualmente autenticado.

- **RF06 - Logout:**  
  O sistema deve permitir que o usuário efetue logout, encerrando sua sessão.

- **RF07 - E-mail Único:**  
  O sistema deve impedir o cadastro de usuários com e-mails duplicados.

- **RF08 - Proteção de Rotas:**  
  O sistema deve proteger rotas sensíveis, permitindo acesso apenas a usuários autenticados.

- **RF09 - API REST:**  
  O sistema deve disponibilizar endpoints REST para cadastro, login, consulta de perfil e logout.

- **RF10 - Esqueci Minha Senha:**  
  O sistema deve permitir que o usuário solicite a redefinição de senha, enviando um e-mail com um link de recuperação.

- **RF11 - Troca de Senha via E-mail:**  
  O sistema deve permitir que o usuário altere sua senha por meio de um link enviado ao seu e-mail, garantindo segurança no processo.

- **RF12 - Cadastro de Produto:**  
  O sistema deve permitir que usuários administradores cadastrem novos produtos, informando nome, descrição, quantidade, unidade, valor e categoria.

- **RF13 - Edição de Produto:**  
  O sistema deve permitir que usuários administradores editem informações dos produtos cadastrados.

- **RF14 - Remoção de Produto:**  
  O sistema deve permitir que usuários administradores removam produtos do estoque.

- **RF15 - Consulta de Estoque:**  
  O sistema deve permitir que todos os usuários consultem a lista de produtos em estoque, com opções de filtro e busca.

- **RF16 - Entrada de Estoque:**  
  O sistema deve permitir que usuários adicionem entradas de estoque, registrando quantidade, data e responsável.

- **RF17 - Saída de Estoque:**  
  O sistema deve permitir que usuários registrem saídas de produtos do estoque, informando quantidade, data e responsável.

- **RF18 - Histórico de Movimentação:**  
  O sistema deve registrar toda movimentação de estoque (entradas e saídas), permitindo consulta por período, produto e usuário.

- **RF19 - Perfis de Usuário:**  
  O sistema deve permitir dois tipos de usuários: Administrador (com acesso total) e Assistente (acesso restrito).

- **RF20 - Permissões:**  
  O sistema deve garantir que apenas administradores possam cadastrar, editar ou remover produtos; assistentes só podem consultar e registrar movimentações.

- **RF21 - Relatórios:**  
  O sistema deve permitir a geração de relatórios de estoque, movimentações e produtos mais/menos movimentados.

---

### Requisitos Não Funcionais (RNF)

- **RNF01 - Segurança de Dados:**  
  O sistema deve garantir a segurança dos dados dos usuários, utilizando HTTPS e boas práticas de desenvolvimento seguro.

- **RNF02 - Performance:**  
  O sistema deve ser capaz de responder às requisições em até 2 segundos, em condições normais de uso.

- **RNF03 - Compatibilidade:**  
  O sistema deve ser compatível com os principais navegadores modernos, se houver frontend.

- **RNF04 - Escalabilidade:**  
  O sistema deve permitir, de forma simples, a adição de novos recursos e endpoints futuros.

- **RNF05 - Integridade:**  
  O sistema não deve permitir operações que resultem em inconsistência no banco de dados, como cadastro duplicado.

- **RNF06 - Testes de Segurança:**  
  O sistema deve ser testado contra vulnerabilidades comuns (ex: SQL Injection, XSS, CSRF).

- **RNF07 - Documentação:**  
  O sistema deve possuir documentação dos endpoints e das regras de negócio.

- **RNF08 - Auditoria:**  
  Toda tentativa de login deve ser registrada, incluindo sucessos e falhas, para fins de auditoria.

- **RNF09 - Controle de Acesso:**  
  O sistema deve implementar controle de acesso baseado em papéis (RBAC), garantindo que permissões sejam respeitadas em todos os endpoints.

- **RNF10 - Proteção contra SQL Injection:**  
  O sistema deve utilizar ORM (ex: JPA/Hibernate) e nunca concatenar consultas SQL manualmente.

- **RNF11 - Proteção contra XSS:**  
  O sistema deve sanitizar todas as entradas de usuário e escapar dados exibidos no front-end.

- **RNF12 - Proteção contra CSRF:**  
  O sistema deve implementar proteção contra CSRF nos formulários e endpoints sensíveis.

- **RNF13 - Registro de Auditoria:**  
  Toda ação de cadastro, edição, remoção e movimentação de estoque deve ser registrada em logs de auditoria, incluindo usuário, data e detalhes da ação.

- **RNF14 - Senhas Fortes:**  
  O sistema deve exigir que senhas de usuários atendam a critérios mínimos de complexidade.

- **RNF15 - Bloqueio após Tentativas de Login:**  
  O sistema deve bloquear temporariamente o acesso de usuários após múltiplas tentativas falhas de login, prevenindo ataques de força bruta.

- **RNF16 - Backup de Dados:**  
  O sistema deve permitir backup periódico do banco de dados para evitar perda de informações.

- **RNF17 - Validação de Entrada:**  
  Todas as entradas de dados devem ser validadas tanto no front-end quanto no back-end.

---

## Estrutura do Banco de Dados

O sistema utiliza um banco relacional (MariaDB/MySQL) para armazenar informações de usuários, permissões (roles), tokens revogados e controle de acesso. Abaixo estão as principais tabelas e seus respectivos campos:

### Tabelas principais

#### `usuarios`
Armazena dados dos usuários cadastrados.

| Campo   | Tipo           | Descrição              |
|---------|----------------|------------------------|
| id      | bigint (PK)    | Identificador único    |
| nome    | varchar(255)   | Nome do usuário        |
| email   | varchar(255)   | E-mail (único)         |
| senha   | varchar(255)   | Senha criptografada    |
| role    | varchar(50)    | Perfil do usuário      |

- Índice único no campo `email` para garantir que não haja duplicidade de e-mails.

#### `roles`
Tabela de perfis/permissões do sistema.

| Campo   | Tipo           | Descrição              |
|---------|----------------|------------------------|
| id      | int (PK)       | Identificador da role  |
| nome    | varchar(100)   | Nome do perfil (ex: ADMIN, ASSISTENTE) |

- Índice único no campo `nome`.

#### `usuarios_roles`
Relaciona usuários aos seus perfis/roles.

| Campo       | Tipo         | Descrição                     |
|-------------|--------------|-------------------------------|
| usuario_id  | bigint (PK)  | Referência ao usuário         |
| role_id     | int (PK)     | Referência ao perfil/role     |

- Chave primária composta (`usuario_id`, `role_id`).

#### `revoked_tokens`
Tabela para armazenar tokens JWT revogados (logout, expiração manual).

| Campo          | Tipo          | Descrição                   |
|----------------|---------------|-----------------------------|
| id             | bigint (PK)   | Identificador único         |
| token          | text          | Token JWT revogado          |
| data_expiracao | datetime      | Data de expiração do token  |

- Índice no campo `token` para busca rápida.

---

### Considerações

- **Integridade referencial:** As tabelas de relacionamento garantem que um usuário pode ter múltiplos perfis e que as permissões são gerenciadas de forma flexível.
- **Segurança:** Senhas devem ser armazenadas criptografadas. Tokens revogados ajudam a evitar reuso indevido após logout ou troca de senha.
- **Expansão:** A estrutura permite fácil adição de novos tipos de perfis e controle granular de acesso.

> Para ver o script completo de criação das tabelas, acesse [`sistemalogin.sql`](https://github.com/Vatsu04/Sistema-para-TCC/blob/d556f797709ddba6291aae7f7f6c4369c1c28754/sistemalogin.sql).


## Vulnerabilidades comuns e como mitigar no sistema

- **SQL Injection:**  
  Use JPA/Hibernate, nunca construa queries SQL manualmente com concatenação de strings.

- **XSS (Cross-Site Scripting):**  
  Escape e sanitize todas as saídas e entradas de dados que vão para o front-end.

- **CSRF (Cross-Site Request Forgery):**  
  Use tokens CSRF nas requisições, especialmente em POST, PUT, DELETE.

- **Brute Force:**  
  Limite tentativas de login e implemente bloqueio temporário.

- **Exposição de Dados Sensíveis:**  
  Nunca retorne informações sensíveis em respostas da API. Use DTOs com dados mínimos necessários.

- **Gestão de Sessão:**  
  Use JWT seguro, configure expiração e revogação adequada.

---

## Perfis de Usuário

- **Administrador**
  - Cadastra, edita, remove produtos.
  - Visualiza e gera relatórios.
  - Gerencia usuários assistentes.
  - Consulta e registra movimentações.

- **Assistente**
  - Consulta estoque.
  - Registra movimentações de entrada/saída.
  - Visualiza histórico.

---

## Como implementar o fluxo de "Esqueci minha senha" e troca de senha via e-mail no Spring Boot

1. **Endpoint para solicitar recuperação de senha**
    - O usuário informa o e-mail cadastrado.
    - O sistema gera um token único de recuperação (UUID, por exemplo), com validade limitada (ex: 1 hora).
    - O token e a data de expiração são salvos no banco, associados ao usuário.
    - O sistema envia um e-mail ao usuário com um link para redefinir a senha. Exemplo de link:  
      `https://seusite.com/reset-password?token=SEU_TOKEN_AQUI`

2. **Envio de e-mail**
    - Utilize o [JavaMailSender](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/mail/javamail/JavaMailSender.html) fornecido pelo Spring Boot para enviar e-mails.
    - Configure as propriedades SMTP (`application.properties`), usando um serviço como Gmail, Mailtrap ou qualquer outro provider de e-mail.

3. **Endpoint para redefinir a senha**
    - O usuário acessa o link recebido por e-mail e informa a nova senha.
    - O sistema verifica se o token é válido e não expirou.
    - Se válido, atualiza a senha do usuário (salvando com hash) e remove/invalida o token.

4. **Exemplo básico de fluxo em código**
    - Gere o token: `UUID.randomUUID().toString()`
    - Salve o token e expiração numa tabela, ex: `password_reset_token`
    - Envie o e-mail usando JavaMailSender:

    ```java
    @Autowired
    private JavaMailSender mailSender;

    public void sendResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Redefinição de senha");
        message.setText("Clique no link para redefinir sua senha: https://seusite.com/reset-password?token=" + token);
        mailSender.send(message);
    }
    ```

5. **Boas práticas**
    - O token deve ser de uso único e expirar após curto período.
    - Nunca envie a senha por e-mail.
    - Sempre armazene a nova senha de forma criptografada (BCrypt).

---

**Dica:** Para testes em desenvolvimento, use serviços como [Mailtrap](https://mailtrap.io/) ou [Ethereal Email](https://ethereal.email/) para simular o envio de e-mails sem precisar de um e-mail real.

---

## Estrutura Recomendada do Projeto

```
Sistema-para-TCC/
  ├── src/             # Código fonte do back-end (Spring Boot)
  ├── pom.xml
  ├── frontend/        # Código fonte do front-end (React, Angular ou Vue)
      ├── package.json
      ├── src/
      └── ...
  └── README.md
```

---

## Documentação dos Endpoints

> Verifique a pasta `/docs` ou utilize ferramentas como Swagger/OpenAPI para consultar endpoints e exemplos de uso.

---

## Como rodar o projeto

**Back-end:**
```bash
mvn spring-boot:run
```

**Front-end (exemplo com React):**
```bash
cd frontend
npm install
npm start
```

---

## Licença

Este projeto é destinado exclusivamente para fins acadêmicos e estudos.
