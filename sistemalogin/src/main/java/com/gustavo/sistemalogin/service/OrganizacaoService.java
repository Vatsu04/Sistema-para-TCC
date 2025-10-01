package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.OrganizacaoCreateDTO;
import com.gustavo.sistemalogin.dto.OrganizacaoResponseDTO;
import com.gustavo.sistemalogin.dto.OrganizacaoUpdateDTO;
import com.gustavo.sistemalogin.model.Organizacao;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.OrganizacaoRepository;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizacaoService {

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public OrganizacaoResponseDTO criarOrganizacao(OrganizacaoCreateDTO dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Organizacao organizacao = new Organizacao();
        organizacao.setNome(dto.getNome());
        organizacao.setCnpj(dto.getCnpj());
        organizacao.setTelefone(dto.getTelefone());
        organizacao.setEmail(dto.getEmail());
        organizacao.setEndereco(dto.getEndereco());
        organizacao.setUser(user);


        Organizacao savedOrganizacao = organizacaoRepository.save(organizacao);
        return new OrganizacaoResponseDTO(savedOrganizacao);
    }

    @Transactional(readOnly = true)
    public List<OrganizacaoResponseDTO> findOrganizacoesByUser(String userEmail) {
        return organizacaoRepository.findByUserEmail(userEmail).stream()
                .map(OrganizacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrganizacaoResponseDTO atualizarOrganizacao(Long id, OrganizacaoUpdateDTO dto, String userEmail) {
        Organizacao organizacao = organizacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organização não encontrada"));

        if (!organizacao.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: você não pode alterar esta organização.");
        }

        if (dto.getNome() != null) organizacao.setNome(dto.getNome());
        if (dto.getCnpj() != null) organizacao.setCnpj(dto.getCnpj());
        if (dto.getTelefone() != null) organizacao.setTelefone(dto.getTelefone());
        if (dto.getEmail() != null) organizacao.setEmail(dto.getEmail());
        if (dto.getEndereco() != null) organizacao.setEndereco(dto.getEndereco());

        Organizacao updatedOrganizacao = organizacaoRepository.save(organizacao);
        return new OrganizacaoResponseDTO(updatedOrganizacao);
    }

    @Transactional
    public void deletarOrganizacao(Long id, String userEmail) {
        Organizacao organizacao = organizacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organização não encontrada"));

        if (!organizacao.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: você não pode deletar esta organização.");
        }

        organizacaoRepository.delete(organizacao);
    }
}
