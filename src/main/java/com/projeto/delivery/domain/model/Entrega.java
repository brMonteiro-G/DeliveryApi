package com.projeto.delivery.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.projeto.delivery.domain.ValidationGroups;
import com.projeto.delivery.domain.exception.DomainException;

@Entity
public class Entrega {

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id; 
	
	@Valid
	@ConvertGroup(from = Default.class,  to = ValidationGroups.ClienteId.class )
	@NotNull
	@ManyToOne
	private Cliente cliente; 
	
	@NotNull
	@Valid
	@Embedded
	private Destinatario destinatario;
	
	@NotNull
	private BigDecimal taxa;
	
	@OneToMany(mappedBy="entrega", cascade = CascadeType.ALL)
	private List<Ocorrencia> ocorrencias = new ArrayList<>();
	
	@JsonProperty(access = Access.READ_ONLY)
	@Column(name="status_entrega")
	@Enumerated(EnumType.STRING) 
	// define o retorno do enum como apenas String, para que não haja retornos como o índice da enumeração por exemplo.  
	private StatusEntrega statusEntrega; 
	
	@JsonProperty(access = Access.READ_ONLY)
	private OffsetDateTime dataPedido; 
	
	@JsonProperty(access = Access.READ_ONLY)
	private OffsetDateTime dataFinalizacao;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Destinatario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Destinatario destinatario) {
		this.destinatario = destinatario;
	}

	public BigDecimal getTaxa() {
		return taxa;
	}

	public void setTaxa(BigDecimal taxa) {
		this.taxa = taxa;
	}

	public StatusEntrega getStatusEntrega() {
		return statusEntrega;
	}

	public void setStatusEntrega(StatusEntrega statusEntrega) {
		this.statusEntrega = statusEntrega;
	}

	public OffsetDateTime getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(OffsetDateTime dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public OffsetDateTime getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(OffsetDateTime dataPedido) {
		this.dataPedido = dataPedido;
	}

	public List<Ocorrencia> getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(List<Ocorrencia> ocorrencias) {
		this.ocorrencias = ocorrencias;
	} 
	

	@Override
	public int hashCode() {
		return Objects.hash(cliente, dataFinalizacao, dataPedido, destinatario, id, statusEntrega, taxa);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entrega other = (Entrega) obj;
		return Objects.equals(cliente, other.cliente) && Objects.equals(dataFinalizacao, other.dataFinalizacao)
				&& Objects.equals(dataPedido, other.dataPedido) && Objects.equals(destinatario, other.destinatario)
				&& Objects.equals(id, other.id) && statusEntrega == other.statusEntrega && Objects.equals(taxa, other.taxa);
	}	

	public Ocorrencia adicionarOcorrencia(String descricao) {
		Ocorrencia ocorrencia = new Ocorrencia();
		ocorrencia.setDescricao(descricao);
		ocorrencia.setDataRegistro(OffsetDateTime.now());
		ocorrencia.setEntrega(this);
		
		this.getOcorrencias().add(ocorrencia);
		
		return ocorrencia;
	}


	public void finalizar() {
		if (naoPodeSerFinalizada()) {
			throw new DomainException("Entrega não pode ser finalizada");
		}
		
		setStatusEntrega(StatusEntrega.FINALIZADA);
		setDataFinalizacao(OffsetDateTime.now());
	}

	private boolean podeSerFinalizada() {
		return StatusEntrega.PENDENTE.equals(getStatusEntrega());
	}
	
	public boolean naoPodeSerFinalizada() {
		return !podeSerFinalizada();
	}
}
