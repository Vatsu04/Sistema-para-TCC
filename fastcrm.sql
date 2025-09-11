-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 12/09/2025 às 00:08
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
-- Estrutura para tabela `funis`
--

CREATE TABLE `funis` (
  `id` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `negocios`
--

CREATE TABLE `negocios` (
  `id` bigint(20) NOT NULL,
  `etapa` varchar(255) NOT NULL,
  `id_ornizacao` int(11) DEFAULT NULL,
  `pipeline_stage` int(11) DEFAULT NULL,
  `titulo` varchar(255) NOT NULL,
  `funil_id` bigint(20) NOT NULL,
  `pessoa_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `password_reset_tokens`
--

CREATE TABLE `password_reset_tokens` (
  `id` bigint(20) NOT NULL,
  `expiration_date` datetime(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  `used` bit(1) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `pessoas`
--

CREATE TABLE `pessoas` (
  `id` bigint(20) NOT NULL,
  `cpf` varchar(255) DEFAULT NULL,
  `data_de_nascimento` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nome` varchar(255) NOT NULL,
  `rg` varchar(255) DEFAULT NULL,
  `telefone` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `roles`
--

CREATE TABLE `roles` (
  `Id` int(11) NOT NULL,
  `Descricao` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `perfil_usuario` int(11) NOT NULL,
  `ativo` bit(1) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `senha` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `users`
--

CREATE TABLE `users` (
  `Id` int(11) NOT NULL,
  `Nome` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Senha` varchar(255) NOT NULL,
  `Ativo` int(11) NOT NULL,
  `PerfilUsuario` int(11) NOT NULL,
  `SenhaProvisoria` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `audit_logs`
--
ALTER TABLE `audit_logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKbeugsu8x48jtqc54tp64qyusd` (`usuario_id`);

--
-- Índices de tabela `funis`
--
ALTER TABLE `funis`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Índices de tabela `negocios`
--
ALTER TABLE `negocios`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKh1cvqctg25l78pw3qb53ilhbl` (`funil_id`),
  ADD KEY `FK747sws8gn5xtweik1fmw35o1l` (`pessoa_id`);

--
-- Índices de tabela `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK71lqwbwtklmljk3qlsugr1mig` (`token`),
  ADD UNIQUE KEY `UKla2ts67g4oh2sreayswhox1i6` (`user_id`);

--
-- Índices de tabela `pessoas`
--
ALTER TABLE `pessoas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKouqc5requard3nhnb6u3wvksm` (`email`),
  ADD KEY `FKehrxbeorjtedi1wr2plj6uboe` (`user_id`);

--
-- Índices de tabela `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`Id`);

--
-- Índices de tabela `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`);

--
-- Índices de tabela `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `Email` (`Email`),
  ADD KEY `PerfilUsuario` (`PerfilUsuario`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `audit_logs`
--
ALTER TABLE `audit_logs`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `funis`
--
ALTER TABLE `funis`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `negocios`
--
ALTER TABLE `negocios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `pessoas`
--
ALTER TABLE `pessoas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `roles`
--
ALTER TABLE `roles`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `users`
--
ALTER TABLE `users`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `audit_logs`
--
ALTER TABLE `audit_logs`
  ADD CONSTRAINT `FKbeugsu8x48jtqc54tp64qyusd` FOREIGN KEY (`usuario_id`) REFERENCES `user` (`id`);

--
-- Restrições para tabelas `funis`
--
ALTER TABLE `funis`
  ADD CONSTRAINT `funis_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`Id`) ON DELETE CASCADE;

--
-- Restrições para tabelas `negocios`
--
ALTER TABLE `negocios`
  ADD CONSTRAINT `FK747sws8gn5xtweik1fmw35o1l` FOREIGN KEY (`pessoa_id`) REFERENCES `pessoas` (`id`),
  ADD CONSTRAINT `FKh1cvqctg25l78pw3qb53ilhbl` FOREIGN KEY (`funil_id`) REFERENCES `funis` (`id`);

--
-- Restrições para tabelas `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD CONSTRAINT `FK20xweju6fxkxcx3taa9elhtew` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Restrições para tabelas `pessoas`
--
ALTER TABLE `pessoas`
  ADD CONSTRAINT `FKehrxbeorjtedi1wr2plj6uboe` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Restrições para tabelas `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`PerfilUsuario`) REFERENCES `roles` (`Id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
