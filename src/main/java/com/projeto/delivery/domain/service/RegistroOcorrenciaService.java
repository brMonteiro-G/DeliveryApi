package com.projeto.delivery.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.delivery.domain.model.Entrega;
import com.projeto.delivery.domain.model.Ocorrencia;

@Service
public class RegistroOcorrenciaService {
	
	@Autowired
	private BuscaEntregaService buscaEntregaService; 
	
	@Transactional
	public Ocorrencia registrar(Long entregaId, String descricao ) {
		Entrega entrega =  buscaEntregaService.buscar(entregaId);
		return entrega.adicionarOcorrencia(descricao);
	}
	//ñ é necessário chamar o método save pois o objeto entrega já está sendo persistido e o jakarta persistence já sincroniza com ocorrencias
	
}
