package br.coop.integrada.api.pa.domain.modelDto.imovel;

import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.imovel.ImovelProdutor;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ImovelZoomDto implements Serializable {

    private Long cadpro;
    private Long matricula;
    private String nome;
    private String cidade;
    private String estado;

    public static ImovelZoomDto construir(ImovelProdutor obj) {
        var objDto = new ImovelZoomDto();;
        BeanUtils.copyProperties(obj, objDto);
        BeanUtils.copyProperties(obj.getImovel(), objDto);

        return objDto;
    }

    public static List<ImovelZoomDto> construir(List<ImovelProdutor> objs) {
        if(objs == null) return new ArrayList<>();

        return objs.stream().map(imovelProdutor -> {
            return construir(imovelProdutor);
        }).toList();
    }
}
