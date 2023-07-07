package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.domain.model.produto.Produto;
import lombok.Data;

@Data
public class ProdutoGetDto {
    private Long id;    
    private String codItem; 
    private String descItem;
    private Collection<ReferenciaGrupoDto> referencia = new ArrayList<>();

    public static ProdutoGetDto construir(Produto obj) {
        var objDto = new ProdutoGetDto();
        BeanUtils.copyProperties(obj, objDto);

        List<ReferenciaGrupoDto> referencias = ReferenciaGrupoDto.construir(obj.getReferencias());
        objDto.setReferencia(referencias);

        return objDto;
    }

    public static List<ProdutoGetDto> construir(List<Produto> objs) {
        if(objs == null) return new ArrayList<>();

        return objs.stream().map(produto -> {
            return construir(produto);
        }).toList();
    }
}
