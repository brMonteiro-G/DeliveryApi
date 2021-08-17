package com.projeto.delivery.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.delivery.domain.exception.DomainException;
import com.projeto.delivery.domain.model.Cliente;
import com.projeto.delivery.domain.model.Entrega;
import com.projeto.delivery.domain.repository.ClienteRepository;

@Service
public class CatalogoClienteService {

	@Autowired
	ClienteRepository clienteRepository; 
	

	public Cliente buscar(Long clienteId) {
		return clienteRepository.findById(clienteId).orElseThrow(() -> new DomainException("Cliente não encontrado!"));
	}
	
	
	@Transactional //declara que o método deve ser executado dentro de uma transação 
	public Cliente salvar(Cliente cliente) {
		
		boolean emailEmUso = clienteRepository.findByEmail(cliente.getEmail())
				.stream()                                  //parâmetro é do tipo predicate vindo da interface funcional 
				.anyMatch(clienteExistente -> !clienteExistente.equals(cliente)); //match irá gerar true
				
				if(emailEmUso) {
					throw new DomainException("Já existe um cliente cadastrado com esse email");
				}
		
		return clienteRepository.save(cliente);
	}
	
	@Transactional
	public void excluir(Long id) {
		 clienteRepository.deleteById(id);
	}
}
