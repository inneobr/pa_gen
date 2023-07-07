package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaCarenciaArmazenagem;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TaxaCarenciaArmazenagemDto implements Serializable {

    private Long id;

    @ComparaObjeto(nome="Data inicial")
    private Date dataInicial;

    @ComparaObjeto(nome="Data final")
    private Date dataFinal;

    @ComparaObjeto(nome="Quantidade de dias carência")
    private Integer quantidadeDiasCarencia;

    public static TaxaCarenciaArmazenagemDto construir(TaxaCarenciaArmazenagem obj) {
        var objDto = new TaxaCarenciaArmazenagemDto();
        BeanUtils.copyProperties(obj, objDto);
        return objDto;
    }

    public static List<TaxaCarenciaArmazenagemDto> construir(List<TaxaCarenciaArmazenagem> objs) {
        if(objs == null) return new ArrayList<>();
        return objs.stream().map(taxaCarenciaArmazenagem -> {
            return TaxaCarenciaArmazenagemDto.construir(taxaCarenciaArmazenagem);
        }).collect(Collectors.toList());
    }

    public static TaxaCarenciaArmazenagem converterDto(TaxaCarenciaArmazenagemDto objDto, Taxa taxa) {
        var obj = new TaxaCarenciaArmazenagem();
        BeanUtils.copyProperties(objDto, obj);

        obj.setTaxa(taxa);

        return obj;
    }

    public static List<TaxaCarenciaArmazenagem> converterDto(List<TaxaCarenciaArmazenagemDto> objDtos, Taxa taxa) {
        if(objDtos == null) return new ArrayList<>();
        return objDtos.stream().map(taxaCarenciaArmazenagemDto -> {
            return TaxaCarenciaArmazenagemDto.converterDto(taxaCarenciaArmazenagemDto, taxa);
        }).collect(Collectors.toList());
    }
}
