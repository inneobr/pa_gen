package br.coop.integrada.api.pa.domain.modelDto.imovel;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.imovel.ImovelProdutor;
import lombok.Data;

@Data
public class ImovelProdutorSimplesDto {
	private Long id;
    private Long matricula; 
    private String nome;
    private String estado;
    private Long cadpro;
    
    public String getMatriculaNome() {
        return matricula + " - " + nome;
    }
    
    public String getMatriculaNomeCadpro() {
    	if(cadpro == null) {
    		return matricula + " - " + nome; 
    	}
    	
        return matricula + " - " + nome + " - " + cadpro;
    }
    
    public static ImovelProdutorSimplesDto construir(ImovelProdutor obj) {
    	var objDto = new ImovelProdutorSimplesDto();
    	
    	objDto.setCadpro(obj.getCadpro());
    	
    	if(obj.getImovel() != null) {
    		BeanUtils.copyProperties(obj.getImovel(), objDto);
    	}
    	
    	return objDto;
    }

	public static List<ImovelProdutorSimplesDto> construir(List<ImovelProdutor> objs) {
		if(CollectionUtils.isEmpty(objs)) return Collections.emptyList();
		return objs.stream().map(imovel -> {
			return construir(imovel);
		}).toList();
	}

	public static ImovelProdutorSimplesDto construir(Imovel imovel) {
    	var objDto = new ImovelProdutorSimplesDto();
		BeanUtils.copyProperties(imovel, objDto);
    	
    	return objDto;
	}
}
