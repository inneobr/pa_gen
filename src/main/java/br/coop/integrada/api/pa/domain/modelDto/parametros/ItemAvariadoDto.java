package br.coop.integrada.api.pa.domain.modelDto.parametros;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoEstabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoSimplesDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemAvariadoDto implements Serializable {

    private Long id;

    @NotNull(message = "Campo {codigo} obrigatório")
    @NotBlank(message = "Campo {codigo} obrigatório")
    @ComparaObjeto(nome="Descrição")
    private String descricao;

    @ComparaObjeto(nome="Campo validação")
    private ItemAvariadoValidacaoEnum campoValidacao;
    private List<ItemAvariadoDetalheDto> detalhes;
    private List<EstabelecimentoSimplesDto> estabelecimentos;
    private Date dataCadastro;
    private Date dataAtualizacao;
    private Boolean ativo;

    public static ItemAvariadoDto construir(ItemAvariado obj) {
        var objDto = new ItemAvariadoDto();
        BeanUtils.copyProperties(obj, objDto);

        List<ItemAvariadoDetalheDto> detalhes = ItemAvariadoDetalheDto.construir(obj.getDetalhes());
        Collections.sort(detalhes, Comparator.comparing(ItemAvariadoDetalheDto::getPercentualInicial));
        objDto.setDetalhes(detalhes);

        if(CollectionUtils.isEmpty(obj.getEstabelecimentos())) {
            objDto.setEstabelecimentos(new ArrayList<>());
        }
        else {
            List<EstabelecimentoSimplesDto> estabelecimentos = obj.getEstabelecimentos().stream().map(itemAvariadoEstabelecimento -> {
                return EstabelecimentoSimplesDto.construir(itemAvariadoEstabelecimento.getEstabelecimento());
            }).toList();
            objDto.setEstabelecimentos(estabelecimentos);
        }

        return objDto;
    }

    public static List<ItemAvariadoDto> construir(List<ItemAvariado> objs) {
        return objs.stream().map(itemAvariado -> {
            return ItemAvariadoDto.construir(itemAvariado);
        }).collect(Collectors.toList());
    }

    public static ItemAvariado converterDto(ItemAvariadoDto objDto) {
        var obj = new ItemAvariado();
        BeanUtils.copyProperties(objDto, obj);

        List<ItemAvariadoDetalhe> detalhes = ItemAvariadoDetalheDto.converterDto(objDto.getDetalhes(), obj);
        obj.setDetalhes(detalhes);

        List<ItemAvariadoEstabelecimento> estabelecimentos = objDto.getEstabelecimentos().stream().map(estabeleecimentoDto -> {
            Estabelecimento estabelecimento = EstabelecimentoSimplesDto.converterDto(estabeleecimentoDto);
            var itemAvariadoEstabelecimento = new ItemAvariadoEstabelecimento();
            itemAvariadoEstabelecimento.setItemAvariado(obj);
            itemAvariadoEstabelecimento.setEstabelecimento(estabelecimento);
            return itemAvariadoEstabelecimento;
        }).toList();
        obj.setEstabelecimentos(estabelecimentos);

        return obj;
    }
}
