package br.coop.integrada.api.pa.domain.modelDto.grupoProduto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import lombok.Data;

@Data
public class GrupoProdutoSimplesDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

    @NotBlank(message = "Campo {fmCodigo} obrigatório")
    private String fmCodigo;

    @NotBlank(message = "Campo {descricao} obrigatório")
    private String descricao;
    
    private Boolean fnt;
    private Boolean ativo;
	
	public String getCodigoDescricao() {
        return fmCodigo + " - " + descricao;
    }

    public static GrupoProdutoSimplesDto construir(GrupoProduto obj) {
        var objDto = new GrupoProdutoSimplesDto();
        BeanUtils.copyProperties(obj, objDto);
        return objDto;
    }

    public static List<GrupoProdutoSimplesDto> construir(List<GrupoProduto> objs) {
        if(CollectionUtils.isEmpty(objs)) return Collections.emptyList();
        return objs.stream().map(grupoProduto -> {
        	return construir(grupoProduto);
        }).toList();
    }

    public static GrupoProduto converterDto(GrupoProdutoSimplesDto objDto) {
        var obj = new GrupoProduto();
        BeanUtils.copyProperties(objDto, obj);
        return obj;
    }
}
