package com.projeto.delivery.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.delivery.api.mapper.EntregaMapper;
import com.projeto.delivery.api.model.DestinatarioModel;
import com.projeto.delivery.api.model.EntregaModel;
import com.projeto.delivery.api.model.input.EntregaInput;
import com.projeto.delivery.domain.model.Entrega;
import com.projeto.delivery.domain.repository.EntregaRepository;
import com.projeto.delivery.domain.service.CatalogoClienteService;
import com.projeto.delivery.domain.service.FinalizacaoEntregaService;
import com.projeto.delivery.domain.service.SolicitacaoEntregaService;

@RestController
@RequestMapping("/entregas")
public class EntregaController {
	
	@Autowired
	private EntregaMapper entregaMapper; ; 

	@Autowired
	private SolicitacaoEntregaService solicitacaoEntregaService; 
	
	@Autowired
	private FinalizacaoEntregaService finalizacaoEntregaService; 
	
	@Autowired
	private EntregaRepository entregaRepository;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EntregaModel solicitar( @RequestBody  @Valid EntregaInput entregaInput) {
		
		Entrega novaEntrega = entregaMapper.toEntity(entregaInput);
		
		Entrega EntregaSolicitada = solicitacaoEntregaService.solicitar(novaEntrega);
		
		return entregaMapper.toModel(EntregaSolicitada);
	}
	
	
	@PutMapping("/{entregaId}/finalizacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void finalizar(@PathVariable Long entregaId) {
		finalizacaoEntregaService.finalizar(entregaId);
	}
	
	@GetMapping
	public List<Entrega> listar(){
		return entregaRepository.findAll();
	}
	
	@GetMapping("/{entregaId}")
	public ResponseEntity<EntregaModel> buscar(@PathVariable Long entregaId){
		
		return entregaRepository.findById(entregaId)
				.map(entrega -> ResponseEntity.ok(entregaMapper.toModel(entrega)))
				.orElse(ResponseEntity.notFound().build());
	}
	
}
