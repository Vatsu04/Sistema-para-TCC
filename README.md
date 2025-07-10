# Sistema-para-TCC
Sistema de cadastro e login desenvolvido em Java utilizando Spring Boot, com autenticação segura, recuperação de senha via e-mail e API REST. Projeto voltado para estudos de segurança e boas práticas em aplicações web.

# Requisitos do Sistema de Cadastro e Login

## Requisitos Funcionais (RF)

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

---

## Requisitos Não Funcionais (RNF)

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

Se precisar de exemplos completos de código ou configuração do envio de e-mail, posso ajudar!
