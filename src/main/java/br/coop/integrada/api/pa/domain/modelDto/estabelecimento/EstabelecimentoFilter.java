package br.coop.integrada.api.pa.domain.modelDto.estabelecimento;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EstabelecimentoFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String nomeFantasia;
	public String codigo;
	public String codigoRegional;
	public String razaoSocial;
	public String endereco;
	public String bairro;
	public String cidade;
	public String estado;
	public String email;
	public String telefone;
	public String cnpj;
	public String inscricaoEstadual;
	public String filtroGlobal;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String pesquisar;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Boolean logUbs;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Boolean logSilo;
	
}
