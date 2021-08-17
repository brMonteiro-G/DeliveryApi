package com.projeto.delivery.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.projeto.delivery.domain.model.Cliente;
import com.projeto.delivery.domain.model.Entrega;
import com.projeto.delivery.domain.model.StatusEntrega;
import com.projeto.delivery.domain.repository.EntregaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitacaoEntregaService {

	@Autowired
	private EntregaRepository entregaRepository;
	
	@Autowired
	private CatalogoClienteService catalogoClienteService; 	
	
	
	@Transactional
	public Entrega solicitar(Entrega entrega) {
		
		Cliente cliente = catalogoClienteService.buscar(entrega.getCliente().getId());
		
		entrega.setCliente(cliente);
		entrega.setStatusEntrega(StatusEntrega.PENDENTE);
		entrega.setDataPedido(OffsetDateTime.now());
		
		return entregaRepository.save(entrega); 
	}
	
	
	
}
