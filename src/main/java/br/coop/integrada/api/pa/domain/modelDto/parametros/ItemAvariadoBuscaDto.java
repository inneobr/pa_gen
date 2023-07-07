package br.coop.integrada.api.pa.domain.modelDto.parametros;

import java.io.Serializable;

import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;
import lombok.Data;

@Data
public class ItemAvariadoBuscaDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String codigoProduto;
	private String codigoReferencia;
	
	public static ItemAvariadoBuscaDto construir(ItemAvariadoDetalhe obj) {
		var objDto = new ItemAvariadoBuscaDto();
		
		if(obj.getProduto() != null) {
			objDto.setCodigoProduto(obj.getProduto().getCodItem());
		}
		
		if(obj.getProdutoReferencia() != null) {
			objDto.setCodigoReferencia(obj.getProdutoReferencia().getCodRef());
		}
		
		return objDto;
	}
}
