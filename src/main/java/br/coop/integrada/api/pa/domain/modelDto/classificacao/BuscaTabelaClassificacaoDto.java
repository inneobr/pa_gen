package br.coop.integrada.api.pa.domain.modelDto.classificacao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoDetalhe;
import lombok.Data;

@Data
public class BuscaTabelaClassificacaoDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Boolean cadastrado;
	private BigDecimal percentualDesconto;
	private BigInteger phCorrigido;
	private String codigoReferencia;;
	private BigDecimal taxaSecagemQuilo;
	private BigDecimal taxaSecagemValor;
	
	public static BuscaTabelaClassificacaoDto construir(Boolean cadastrado, ClassificacaoDetalhe detalhe) {
		var objDto = new BuscaTabelaClassificacaoDto();
		objDto.setCadastrado(cadastrado);
		
		if(detalhe != null) {
			BeanUtils.copyProperties(detalhe, objDto);
			
			if(detalhe.getProdutoReferencia() != null) {
				String codigoReferencia = detalhe.getProdutoReferencia().getCodRef(); 
				objDto.setCodigoReferencia(codigoReferencia);
			}
		}
		
		return objDto;
	}
}
