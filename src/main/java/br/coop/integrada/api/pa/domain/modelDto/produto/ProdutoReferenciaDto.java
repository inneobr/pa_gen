package br.coop.integrada.api.pa.domain.modelDto.produto;

import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProdutoReferenciaDto {

    
    @NotNull
    private Long id;
    //private String codReg;  
    //private String codItem; 
    private String codRef;
    private String idErp;
    private Boolean ativo;
    
	//@Override
	//public String toString() {
	//	return codRef;
	//}

	public static ProdutoReferenciaDto construir(ProdutoReferencia obj) {
		var objDto = new ProdutoReferenciaDto();
		BeanUtils.copyProperties(obj, objDto);
		return objDto;
	}

	public static ProdutoReferencia converterDto(ProdutoReferenciaDto objDto) {
		var obj = new ProdutoReferencia();
		BeanUtils.copyProperties(objDto, obj);
		return obj;
	}
}
