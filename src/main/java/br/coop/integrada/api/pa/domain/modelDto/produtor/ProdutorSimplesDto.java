package br.coop.integrada.api.pa.domain.modelDto.produtor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.Produtor;
import lombok.Data;

@Data
public class ProdutorSimplesDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String codProdutor;
    private String nome;
    private String cpfCnpj;
    private Boolean emiteNota;
    private Boolean cooperativa;
    
    public String getMatriculaNome() {
        return codProdutor + " - " + nome;
    }

	public static ProdutorSimplesDto construir(Produtor obj) {
		var objDto = new ProdutorSimplesDto();
		BeanUtils.copyProperties(obj, objDto);
		return objDto;
	}
	
	public static List<ProdutorSimplesDto> construir(List<Produtor> objs) {
		if(CollectionUtils.isEmpty(objs)) return Collections.emptyList();
		
		return objs.stream().map(produtor -> {
			return construir(produtor);
		}).toList();
	}
}
