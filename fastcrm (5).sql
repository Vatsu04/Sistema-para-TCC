-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 08/12/2025 às 03:43
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `fastcrm`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `audit_logs`
--

CREATE TABLE `audit_logs` (
  `id` bigint(20) NOT NULL,
  `acao` varchar(255) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `detalhes` varchar(255) DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `etapa`
--

CREATE TABLE `etapa` (
  `id` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `funil_id` bigint(20) NOT NULL,
  `posicao` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `etapa`
--

INSERT INTO `etapa` (`id`, `nome`, `funil_id`, `posicao`) VALUES
(1, 'Nova Etapa 2', 5, 0),
(4, 'Etapa 1', 4, 0),
(6, 'Etapa 1', 6, 0),
(7, 'Nova Etapa 2', 7, 0),
(8, 'Etapa 1', 8, 0),
(10, 'Etapa 2', 4, 2),
(12, 'Etapa 2', 8, 1),
(13, 'Etapa 2 ', 10, 0),
(14, 'Etapa 3', 10, 1),
(15, 'Etapa Renomeada', 4, 2),
(17, 'Etapa Renomeada', 4, 3),
(19, 'Etapa Assistente', 13, 0);

-- --------------------------------------------------------

--
-- Estrutura para tabela `funis`
--

CREATE TABLE `funis` (
  `id` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `funis`
--

INSERT INTO `funis` (`id`, `nome`, `user_id`) VALUES
(2, 'Funil do Usuario A', 13),
(3, 'Funil do Usuario A', 14),
(4, 'Funil do Gustavo', 15),
(5, 'Novo funil', 15),
(6, 'Funil do Gustavo 2', 15),
(7, 'Funil exemplo', 15),
(8, 'Funil novo', 15),
(10, 'FunilExemploVideoRenomeado', 15),
(13, 'Funil Assistente', 24);

-- --------------------------------------------------------

--
-- Estrutura para tabela `negocios`
--

CREATE TABLE `negocios` (
  `id` bigint(20) NOT NULL,
  `titulo` varchar(255) NOT NULL,
  `funil_id` bigint(20) NOT NULL,
  `organizacao` varchar(255) DEFAULT NULL,
  `data_de_fechamento` date DEFAULT NULL,
  `valor` decimal(38,2) DEFAULT NULL,
  `data_de_abertura` date NOT NULL DEFAULT curdate(),
  `etapa_id` bigint(20) NOT NULL,
  `Pessoa_Contato` varchar(255) NOT NULL,
  `email_pessoa_contato` varchar(255) NOT NULL,
  `telefone_pessoa_contato` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `negocios`
--

INSERT INTO `negocios` (`id`, `titulo`, `funil_id`, `organizacao`, `data_de_fechamento`, `valor`, `data_de_abertura`, `etapa_id`, `Pessoa_Contato`, `email_pessoa_contato`, `telefone_pessoa_contato`) VALUES
(4, 'Negocio exemplo 3', 4, 'Organizacao negocio 3', '2025-12-15', 4000.00, '2025-10-21', 17, 'Pessoa do Negócio Exemplo3', 'pessoa3@gmail.com', '21985812312'),
(9, 'Negocio Renomeado', 10, 'Organização Exemplo', '2025-12-20', 4000.25, '2025-12-05', 13, 'Pessoa de Contato2', 'pessoacontato@gmail.com', '21943249593'),
(10, 'Negocio Renomeado', 4, 'Organização Exemplo', '2025-12-20', 4000.00, '2025-12-06', 10, 'Pessoa Contato Exemplo', 'pcexemplo@gmail.com', '21983125342'),
(11, 'Negocio Renomeado', 4, 'Organização Exemplo', '2025-12-25', 5000.00, '2025-12-06', 10, 'Pessoa Negocio Exemplo', 'pnexemplo@gmail.com', '21948529889');

-- --------------------------------------------------------

--
-- Estrutura para tabela `password_reset_tokens`
--

CREATE TABLE `password_reset_tokens` (
  `id` bigint(20) NOT NULL,
  `token` varchar(255) NOT NULL,
  `expiration_date` datetime NOT NULL,
  `used` tinyint(1) NOT NULL DEFAULT 0,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `password_reset_tokens`
--

INSERT INTO `password_reset_tokens` (`id`, `token`, `expiration_date`, `used`, `user_id`) VALUES
(25, '24f9499c-c7ce-4b5c-8ab8-a209bbf008f9', '2025-12-06 12:36:39', 1, 15);

-- --------------------------------------------------------

--
-- Estrutura para tabela `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `Descricao` varchar(50) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `telefone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `roles`
--

INSERT INTO `roles` (`id`, `Descricao`, `nome`, `email`, `telefone`) VALUES
(1, 'Perfil de Administrador do sistema', 'ADMINISTRADOR', NULL, NULL),
(2, 'Perfil de Assistente com acesso limitado', 'ASSISTENTE', NULL, NULL);

-- --------------------------------------------------------

--
-- Estrutura para tabela `users`
--

CREATE TABLE `users` (
  `Id` bigint(20) NOT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `Senha` varchar(255) NOT NULL,
  `ativo` bit(1) NOT NULL,
  `perfil` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `users`
--

INSERT INTO `users` (`Id`, `nome`, `email`, `Senha`, `ativo`, `perfil`) VALUES
(7, 'Guilherme Suares de Freitas', 'suares.tk@gmail.com', '$2a$10$rpA3YFX7MsDcl.Tfx7niv.axWNHmv/SpSFMyyRELTrAgJopXhjA..', b'1', 1),
(8, 'Gustavo Passos Tascheri', 'gustavo.tascheri@exemplo.com', '$2a$10$QApu5EjVBgRMz3LvHMgPo.8wdTTRcQ.KgWztW7zw8PFhuuVnjIz1u', b'1', 1),
(9, 'Gustavo Passos Tascheri', 'gugatascha@exemplo.com', '$2a$10$9PzvQf8hmS6lQPSjCU88J.03XW5WmBzu1iKHgNhexJfUnilwALMNW', b'1', 2),
(10, 'Gustavo Passos Tascheri', 'gugapassos@exemplo.com', '$2a$10$uK3kIhJlzPv30zBaNhrrfe49M8BQBI1reYJixHnFVYd1B6iaIV5BC', b'1', 2),
(11, 'Usuário Senha Forte', 'forte@exemplo.com', '$2a$10$P7hJoj94vASnmlt8MwItOOXEvgCdWXNZ6cMtpg7.SF/O4T.jTdFvu', b'1', 2),
(12, 'Usuário Senha Fraca', 'fraca@exemplo.com', '$2a$10$yUYVC8aSspKTUwTMmgP4eezqOtbLRXV.6CGUxAMxqMseYkxyUR73C', b'1', 2),
(13, 'Usuario A', 'usuarioA@exemplo.com', '$2a$10$bmTdUMffbfUs/20OjW.h5O4XWknE0rtlRKz3Tx3Lj3NnW5LEvQLd6', b'1', 2),
(14, 'Usuario B', 'usuarioB@exemplo.com', '$2a$10$nAuNQG8uE46I/R.MMB5pW.gBqc7MZgOb.ONWYjvCZmi4k2DjWuXie', b'1', 2),
(15, 'Gustavo Passos Tascheri', 'gugatascheri@gmail.com', '$2a$10$sPU1ei6IV1UPHoevRjDCDOQ55h8BRBeNdlxbI6nU7882kwwVvGQm2', b'1', 1),
(16, 'Teste Atualizar Usuario', 'update@exemplo.com', '$2a$10$PB1iRSUp2MISOBEg0EqYB.V6ykjoEGUMxpU32uMXyYWtFIMwiq1ni', b'0', 2),
(17, 'Silva', '342@gmail.com', '$2a$10$Z5EtjGmq/TUekUJIJPhum./5DwliHhRpU8RuNNKuTkjJePMuP1xGC', b'1', 2),
(18, 'Silva', '3426@gmail.com', '$2a$10$drOkgNz9eEYJf2j4MZ.ZNODJp.T/.Iwjd1Evz4w1Tm7xdJ7ar0CHe', b'1', 2),
(19, 'Caue Bernando', 'caueee@gmail.com', '$2a$10$20/Ty8YYNuPT2t1t0klDjOa01LIkHjQkoLgrGtMzhfDDGuV6cnz.S', b'1', 2),
(20, 'Teste Login Inativo', 'logininativo@gmail.com', '$2a$10$oSRE67HPvlGk2hWWiu3JjuegnBEygXbKJbzqVFC07oy8y5yHv7LEu', b'0', 2),
(21, 'Usuario inativo 2', 'inativo2@gmail.com', '$2a$10$LEvXi18vlbQQZMaMz4VC.eXtQasSWjd8hgUfu/nF1sLekM0/wkzL2', b'0', 2),
(22, 'Suares', 'suaressss@gmail.com', '$2a$10$eVlBQv/RbzxJ6mA.2YqSbOtveDyt4mue2MYQ3HmKHb6.uKjrDurii', b'1', 2),
(23, 'Camilla Lobo Paulino', 'camilla@gmail.com', '$2a$10$tAaEcmuRklwO28NiMVZxAOMpXeaOyf0wFNgcbuZ26TBQ2Flaamq/G', b'1', 1),
(24, 'Usuario Assiste', 'assistente@gmail.com', '$2a$10$ebqQ3EcQBGlah3b.1QQwQuhrkTqk7pKM1O0ONgbY6Qpfpyo3xvfd.', b'1', 2);

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `audit_logs`
--
ALTER TABLE `audit_logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5si1rsyv8ccbi8pk1d66brsc8` (`usuario_id`);

--
-- Índices de tabela `etapa`
--
ALTER TABLE `etapa`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_etapa_to_funis` (`funil_id`);

--
-- Índices de tabela `funis`
--
ALTER TABLE `funis`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKl79jw6cf319wxtmc2gwpio284` (`user_id`);

--
-- Índices de tabela `negocios`
--
ALTER TABLE `negocios`
  ADD PRIMARY KEY (`id`),
  ADD KEY `funil_id` (`funil_id`),
  ADD KEY `fk_negocios_to_etapa` (`etapa_id`);

--
-- Índices de tabela `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `token` (`token`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Índices de tabela `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK7xq7p7jlaaotwefc21s2ecs55` (`nome`);

--
-- Índices de tabela `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `Email` (`email`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `audit_logs`
--
ALTER TABLE `audit_logs`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `etapa`
--
ALTER TABLE `etapa`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT de tabela `funis`
--
ALTER TABLE `funis`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de tabela `negocios`
--
ALTER TABLE `negocios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de tabela `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT de tabela `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de tabela `users`
--
ALTER TABLE `users`
  MODIFY `Id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `audit_logs`
--
ALTER TABLE `audit_logs`
  ADD CONSTRAINT `FK5si1rsyv8ccbi8pk1d66brsc8` FOREIGN KEY (`usuario_id`) REFERENCES `users` (`Id`);

--
-- Restrições para tabelas `etapa`
--
ALTER TABLE `etapa`
  ADD CONSTRAINT `fk_etapa_to_funis` FOREIGN KEY (`funil_id`) REFERENCES `funis` (`id`) ON DELETE CASCADE;

--
-- Restrições para tabelas `funis`
--
ALTER TABLE `funis`
  ADD CONSTRAINT `FKl79jw6cf319wxtmc2gwpio284` FOREIGN KEY (`user_id`) REFERENCES `users` (`Id`),
  ADD CONSTRAINT `funis_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`Id`) ON DELETE CASCADE;

--
-- Restrições para tabelas `negocios`
--
ALTER TABLE `negocios`
  ADD CONSTRAINT `fk_negocios_to_etapa` FOREIGN KEY (`etapa_id`) REFERENCES `etapa` (`id`),
  ADD CONSTRAINT `negocios_ibfk_1` FOREIGN KEY (`funil_id`) REFERENCES `funis` (`id`) ON DELETE CASCADE;

--
-- Restrições para tabelas `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD CONSTRAINT `FK20xweju6fxkxcx3taa9elhtew` FOREIGN KEY (`user_id`) REFERENCES `users` (`Id`),
  ADD CONSTRAINT `password_reset_tokens_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`Id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
