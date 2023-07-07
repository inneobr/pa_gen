package br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacaoEstabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoCodigoDto;
import lombok.Data;

@Data
public class NaturezaOperacaoDto {	
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
	private String modFixSemPjCnfCoop;
	private String natDevFixPjCnfCoop;
	private String serDevFixPjCnfCoop;
	private String modDevFixPjCnfCoop;
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
	private String natFixPjCnfTerc;
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
	private List<EstabelecimentoCodigoDto> listEstabelecimentoDto;
	
	public static NaturezaOperacaoDto construir(NaturezaOperacao naturezaOperacao, List<NaturezaOperacaoEstabelecimento> estabelecimentoList) {
		NaturezaOperacaoDto naturezaOperacaoDto = new NaturezaOperacaoDto();
		BeanUtils.copyProperties(naturezaOperacao, naturezaOperacaoDto);	
		
		if(estabelecimentoList != null) {
			naturezaOperacaoDto.setListEstabelecimentoDto(new ArrayList<>());
			for (NaturezaOperacaoEstabelecimento naturezaOperacaoEstabelecimento : estabelecimentoList) {
			    EstabelecimentoCodigoDto estabelecimentoDto = new EstabelecimentoCodigoDto();
				BeanUtils.copyProperties(naturezaOperacaoEstabelecimento.getEstabelecimento(), estabelecimentoDto);
				naturezaOperacaoDto.getListEstabelecimentoDto().add(estabelecimentoDto);
			}
		}		
		return naturezaOperacaoDto;
	}

}
