package br.coop.integrada.api.pa.domain.modelDto.parametros;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoResumidoDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemAvariadoSimplesDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
    private String descricao;
    private String campoValidacao;
    private Date dataCadastro;
    private Date dataAtualizacao;
    private Boolean ativo;
    private GrupoProdutoResumidoDto grupoProduto;
    private StatusIntegracao statusIntegracao;

    public static ItemAvariadoSimplesDto construir(ItemAvariado obj) {
        var objDto = new ItemAvariadoSimplesDto();
        BeanUtils.copyProperties(obj, objDto);

        if(obj.getCampoValidacao() != null) {
            objDto.setCampoValidacao(obj.getCampoValidacao().getDescricao());
        }
        
        GrupoProdutoResumidoDto grupoProdutoResumidoDto = new GrupoProdutoResumidoDto();
        BeanUtils.copyProperties(obj.getDetalhes().get(0).getProduto().getGrupoProduto(), grupoProdutoResumidoDto);
        objDto.setGrupoProduto(grupoProdutoResumidoDto);

        return objDto;
    }

    public static List<ItemAvariadoSimplesDto> construir(List<ItemAvariado> objs) {
        return objs.stream().map(itemAvariado -> {
            return ItemAvariadoSimplesDto.construir(itemAvariado);
        }).collect(Collectors.toList());
    }
}
