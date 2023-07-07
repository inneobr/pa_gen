package br.coop.integrada.api.pa.domain.modelDto.imovel;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import lombok.Data;

@Data
public class ImovelSimplesDto {
	private Long id;
    private Long matricula; 
    private String nome;
    private String estado;
    
    public String getMatriculaNome() {
        return matricula + " - " + nome;
    }
    
    public static ImovelSimplesDto construir(Imovel obj) {
    	var objDto = new ImovelSimplesDto();
    	BeanUtils.copyProperties(obj, objDto);
    	return objDto;
    }

	public static List<ImovelSimplesDto> construir(List<Imovel> objs) {
		if(CollectionUtils.isEmpty(objs)) return Collections.emptyList();
		return objs.stream().map(imovel -> {
			return construir(imovel);
		}).toList();
	}
}
