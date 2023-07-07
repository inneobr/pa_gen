package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.domain.model.produto.VinculoProdutoReferencia;
import lombok.Data;

@Data
public class ReferenciaGrupoDto {
    private Long id;
    
    private Integer idErp;
    private String codRef;
    

    public static ReferenciaGrupoDto construir(VinculoProdutoReferencia obj) {
        var objDto = new ReferenciaGrupoDto();
        BeanUtils.copyProperties(obj.getProdutoReferencia(), objDto);

        return objDto;
    }

    public static List<ReferenciaGrupoDto> construir(Collection<VinculoProdutoReferencia> objs) {
        if(objs == null) return new ArrayList<>();

        return objs.stream().map(produtoReferencia -> {
            return construir(produtoReferencia);
        }).toList();
    }
}
