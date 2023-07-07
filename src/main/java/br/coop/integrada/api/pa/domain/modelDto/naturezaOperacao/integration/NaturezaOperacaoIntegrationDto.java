package br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import lombok.Data;

@Data
public class NaturezaOperacaoIntegrationDto {	
	@NotNull(message = "Campo {codGrupo} obrigatório")
	private Integer codGrupo;
	
	@NotBlank(message = "Campo {descricao} obrigatório")
	private String descricao;
	
	private String natEntCoop;
	private String serEntCoop;
	private String modNatEntCoop;
	private String natDevEntCoop;
	private String serDevEntCoop;
	private String modDevEntCoop;
	private String natFixCoop;
	private String serFixCoop;
	private String modNatFixCoop;
	private String natFixSemCoop;
	private String serFixSemCoop;
	private String modNatFixSemCoop;
	private String natDevFixCoop;
	private String serDevFixCoop;
	private String modDevFixCoop;
	private String natEntTerc;
	private String serEntTerc;
	private String modNatEntTerc;
	private String natDevEntTerc;
	private String serDevEntTerc;
	private String modDevEntTerc;
	private String natFixTerc;
	private String serFixTerc;
	private String modNatFixTerc;
	private String natFixSemTerc;
	private String serFixSemTerc;
	private String modNatFixSemTerc;
	private String natDevFixTerc;
	private String serDevFixTerc;
	private String modNatDevFixTerc;
	private String natEntPjCnfCoop;
	private String serEntPjCnfCoop;
	private String modNatEntPjCnfCoop;
	private String natDevPjCnfCoop;
	private String serDevPjCnfCoop;
	private String modDevPjCnfCoop;
	private String natFixPjCnfCoop;
	private String serFixPjCnfCoop;
	private String modFixPjCnfCoop;
	private String natFixSemPjCnfCoop;
	private String serFixSemPjCnfCoop;
	private String modFixSemPjCnfCoop;//erro
	private String natDevFixPjCnfCoop;
	private String serDevFixPjCnfCoop;
	private String modDevFixPjCnfCoop;//erro
	private String natEntPjSnfCoop;
	private String serEntPjSnfCoop;
	private String modNatEntPjSnfCoop; 
	private String natDevPjSnfCoop;
	private String serDevPjSnfCoop;
	private String modDevPjSnfCoop;
	private String natFixPjSnfCoop;
	private String serFixPjSnfCoop;
	private String modFixPjSnfCoop;
	private String natFixSemPjSnfCoop;
	private String serFixSemPjSnfCoop;
	private String modFixSemPjSnfCoop;
	private String natDevFixPjSnfCoop;
	private String serDevFixPjSnfCoop;
	private String modDevFixPjSnfCoop;
	private String natEntPjCnfTerc;
	private String serEntPjCnfTerc;
	private String modNatEntPjCnfTerc;
	private String natDevPjCnfTerc;
	private String serDevPjCnfTerc;
	private String modDevPjCnfTerc;
	private String natFixPjCnfTerc;//erro
	private String serFixPjCnfTerc;
	private String modFixPjCnfTerc;
	private String natFixSemPjCnfTerc;
	private String serFixSemPjCnfTerc;
	private String modFixSemPjCnfTerc;
	private String natDevFixPjCnfTerc;
	private String serDevFixPjCnfTerc;
	private String modDevFixPjCnfTerc;
	private String natEntPjSnfTerc;
	private String serEntPjSnfTerc;
	private String modEntPjSnfTerc;
	private String natDevPjSnfTerc;
	private String serDevPjSnfTerc;
	private String modDevPjSnfTerc;
	private String natFixPjSnfTerc;
	private String serFixPjSnfTerc;
	private String modFixPjSnfTerc;
	private String natFixSemPjSnfTec;
	private String serFixSemPjSnfTerc;
	private String modFixSemPjSnfTerc;
	private String natDevFixPjSnfTerc;
	private String serDevFixPjSnfTerc;
	private String modDevFixPjSnfTerc;

	private IntegrationOperacaoEnum operacao;	
	
	public static NaturezaOperacaoIntegrationDto construir(NaturezaOperacao naturezaOperacao) {
		NaturezaOperacaoIntegrationDto naturezaOperacaoIntegrationDto = new NaturezaOperacaoIntegrationDto();
		BeanUtils.copyProperties(naturezaOperacao, naturezaOperacaoIntegrationDto);		
		return naturezaOperacaoIntegrationDto;
	}

}
