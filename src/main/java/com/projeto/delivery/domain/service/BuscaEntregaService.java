package com.projeto.delivery.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.delivery.domain.exception.DomainException;
import com.projeto.delivery.domain.exception.EntidadeNaoEncontradaException;
import com.projeto.delivery.domain.model.Entrega;
import com.projeto.delivery.domain.repository.EntregaRepository;

@Service
public class BuscaEntregaService {

	@Autowired
	private EntregaRepository entregaRepository;
	
	public Entrega buscar(Long entregaId) {
		
		 return entregaRepository.findById(entregaId).orElseThrow( () -> new EntidadeNaoEncontradaException("Entrega não encontrada"));
	}
	
	
}
