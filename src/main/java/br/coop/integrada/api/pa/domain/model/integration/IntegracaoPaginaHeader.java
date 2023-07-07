package br.coop.integrada.api.pa.domain.model.integration;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "integration_page_header")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IntegracaoPaginaHeader implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	private String chave;
	
	@Lob
	private String valor;
	
	private String descricao;
	
	private Boolean desenvolvimento;
	private Boolean homologacao;
	private Boolean producao;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_integracao_page", nullable = false)
	private IntegracaoPagina integracaoPagina;
}
