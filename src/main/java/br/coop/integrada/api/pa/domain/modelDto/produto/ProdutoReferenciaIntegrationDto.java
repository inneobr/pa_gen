package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProdutoReferenciaIntegrationDto {

    private Long id;
        
    @NotNull
    private String codRef; 
	private Boolean ativo;
        
    @Override
    public String toString() {
        return codRef;
    }
    

    public static ProdutoReferenciaDto construir(ProdutoReferencia obj) {
        var objDto = new ProdutoReferenciaDto();
        BeanUtils.copyProperties(obj, objDto);
        objDto.setAtivo(obj.getAtivo());
        return objDto;
    }

    public static ProdutoReferencia converterDto(ProdutoReferenciaDto objDto) {
        var obj = new ProdutoReferencia();
        BeanUtils.copyProperties(objDto, obj);        
        return obj;
    }
    
    public static ProdutoReferencia converterIntegrationDto(ProdutoReferenciaIntegrationDto objDto) {
        var obj = new ProdutoReferencia();
        BeanUtils.copyProperties(objDto, obj);
        
        if(!objDto.getAtivo()) {
        	obj.setDataInativacao(new Date());
        }
        return obj;
    }
    
    
    
    
    
    
}
