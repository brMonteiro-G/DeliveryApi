package com.projeto.delivery.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.delivery.api.mapper.OcorrenciaMapper;
import com.projeto.delivery.api.model.OcorrenciaModel;
import com.projeto.delivery.api.model.input.OcorrenciaInput;
import com.projeto.delivery.domain.model.Entrega;
import com.projeto.delivery.domain.model.Ocorrencia;
import com.projeto.delivery.domain.service.BuscaEntregaService;
import com.projeto.delivery.domain.service.RegistroOcorrenciaService;

@RestController
@RequestMapping("/entregas/{entregaId}/ocorrencias")
public class OcorrenciaController {

	@Autowired
	private RegistroOcorrenciaService registroOcorrenciaService; 
	
	@Autowired
	private  BuscaEntregaService buscaEntregaService; 

	@Autowired
	private OcorrenciaMapper ocorrenciaMapper; 
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OcorrenciaModel registrar(@PathVariable Long entregaId, 
	@Valid @RequestBody OcorrenciaInput ocorrenciaInput	) {
	 Ocorrencia ocorrenciaRegistrada = registroOcorrenciaService.registrar(entregaId, ocorrenciaInput.getDescricao());
	 return ocorrenciaMapper.toModel(ocorrenciaRegistrada);
	}
	
	@GetMapping
	public List<OcorrenciaModel> listar(@PathVariable Long entregaId) {
		Entrega entrega = buscaEntregaService.buscar(entregaId);
		
		return ocorrenciaMapper.toCollectionModel(entrega.getOcorrencias());
	}
	
	
}
