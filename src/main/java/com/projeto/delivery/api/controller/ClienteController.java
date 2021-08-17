package com.projeto.delivery.api.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.delivery.domain.model.Cliente;
import com.projeto.delivery.domain.repository.ClienteRepository;
import com.projeto.delivery.domain.service.CatalogoClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private CatalogoClienteService service; 

	@GetMapping
	public List<Cliente> listar() {
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
		return repository.findById(id)
				.map(cliente -> ResponseEntity.ok(cliente)) // .map(ResponseEntity :: ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente adicionar(@Valid @RequestBody Cliente cliente) {
		return service.salvar(cliente);
	}
	
	@PutMapping("atualiza/{id}")
	public ResponseEntity<Cliente> atualizar (@Valid @PathVariable Long id, @RequestBody Cliente cliente) {
		
		if(repository.existsById(id)) {
			ResponseEntity.notFound().build();
		}
		
		cliente.setId(id);
		cliente  = service.salvar(cliente);
		
		return ResponseEntity.ok(cliente);
		
	}
	
	@DeleteMapping("deleta/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id){
		
		if(repository.existsById(id)) {
			ResponseEntity.notFound().build();
		}
		
		service.excluir(id);
		
		return ResponseEntity.noContent().build();
	}
	
}

