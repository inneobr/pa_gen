package br.coop.integrada.api.pa.domain.modelDto.preco;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.domain.model.preco.Preco;
import lombok.Data;

@Data
public class PrecoBuscaDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal precoFiscal;
	private BigDecimal precoFechamento;
	private LocalDate dataValidade;
	private String horaValidade;
	
	public static PrecoBuscaDto construir(Preco obj, Boolean precoCoco) {
		var objDto = new PrecoBuscaDto();
		BeanUtils.copyProperties(obj, objDto);
		
		if(precoCoco) {
			objDto.setPrecoFechamento(obj.getPrecoFechamentoCoco());
		}
		
		return objDto;
	}
}
