package br.coop.integrada.api.pa.domain.modelDto.produto;

import br.coop.integrada.api.pa.domain.model.produto.Produto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class ProdutoSimplesDto implements Serializable {

    private Long id;
    private String codigo;
    private String descricao;
    private Boolean ativo;

    @Override
    public String toString() {
        return codigo;
    }

    public static ProdutoSimplesDto construir(Produto obj) {
        var objDto = new ProdutoSimplesDto();
        objDto.setId(obj.getId());
        objDto.setCodigo(obj.getCodItem());
        objDto.setDescricao(obj.getDescItem());
        objDto.setAtivo(obj.getAtivo());
        return objDto;
    }

    public static Produto converterDto(ProdutoSimplesDto objDto) {
        var obj = new Produto();
        BeanUtils.copyProperties(objDto, obj);
        return obj;
    }
}
