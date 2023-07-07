package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaEstabelecimento;
import lombok.Data;

@Data
public class TaxaEstabelecimentoDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long idEstabelecimento;
    private String codigo;
    private String codigoRegional;
    private String nomeFantasia;
    
    public static TaxaEstabelecimentoDto construir(TaxaEstabelecimento obj) {
    	var objDto = new TaxaEstabelecimentoDto();
    	BeanUtils.copyProperties(obj.getEstabelecimento(), objDto);
    	
    	objDto.setId(obj.getId());
    	objDto.setIdEstabelecimento(obj.getEstabelecimento().getId());
    	    	
    	return objDto;
    }
    
    public static List<TaxaEstabelecimentoDto> construir(List<TaxaEstabelecimento> objs) {
    	if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();
    	
    	
    	List<TaxaEstabelecimentoDto> estabelecimentos = objs.stream().map(taxaEstabelecimento -> {
    		return construir(taxaEstabelecimento);
    	}).toList();
    	    	
    	return estabelecimentos;
    }    
    
}
