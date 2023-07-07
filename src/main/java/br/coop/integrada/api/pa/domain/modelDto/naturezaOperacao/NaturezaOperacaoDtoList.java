package br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao;

import java.util.Date;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class NaturezaOperacaoDtoList {		
	private Date dataCadastro; 
	private Date dataAtualizacao;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Date dataInativacao;
	
	private Long id;
	private Integer codGrupo;
	private String descricao;
	private Date dataIntegracao;
	
	@Transient
    public Boolean getAtivo() {
        if(dataInativacao == null) {
            return true;
        }
        return false;
    }
}
