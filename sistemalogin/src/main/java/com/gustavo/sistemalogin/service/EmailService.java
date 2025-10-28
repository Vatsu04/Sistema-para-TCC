package com.gustavo.sistemalogin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envia o e-mail de redefinição de senha.
     * @param toEmail O e-mail do destinatário.
     * @param resetLink O link completo de redefinição.
     */
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("seu-email-de-remetente@gmail.com"); // Pode ser o mesmo do application.properties
            message.setTo(toEmail);
            message.setSubject("FastCRM - Redefinição de Senha");

            String emailBody = "Olá,\n\n"
                    + "Você solicitou a redefinição da sua senha no FastCRM.\n"
                    + "Clique no link abaixo para criar uma nova senha:\n\n"
                    + resetLink + "\n\n"
                    + "Se você não solicitou isso, por favor, ignore este e-mail.\n\n"
                    + "Obrigado,\n"
                    + "Equipe FastCRM";

            message.setText(emailBody);

            mailSender.send(message);
            System.out.println("E-mail de redefinição enviado com sucesso para: " + toEmail);

        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail para: " + toEmail);
            e.printStackTrace();

        }
    }
}