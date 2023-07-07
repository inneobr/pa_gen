package br.coop.integrada.api.pa.domain.modelDto.estabelecimento;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EstabelecimentoSimplesDto implements Serializable {
	private static final long serialVersionUID = -2554080066540453575L;
	
	private Long id;
    private String codigo;
    private String codigoRegional;
    private String nomeFantasia;
    
    public String getCodNome(){
    	return codigo + " - " + nomeFantasia;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("ID: ");
        ret.append(id);
        ret.append(" | Código: ");
        ret.append(codigo);
        ret.append(" | Fantasia: ");
        ret.append(nomeFantasia);
        return ret.toString();
    }

    public static EstabelecimentoSimplesDto construir(Estabelecimento obj) {
        EstabelecimentoSimplesDto objDto = new EstabelecimentoSimplesDto();
        BeanUtils.copyProperties(obj, objDto);
        return objDto;
    }

    public static List<EstabelecimentoSimplesDto> construir(List<Estabelecimento> objs) {
        if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();
        return objs.stream().map(estabelecimento -> {
            return EstabelecimentoSimplesDto.construir(estabelecimento);
        }).collect(Collectors.toList());
    }

    public static Estabelecimento converterDto(EstabelecimentoSimplesDto objDto) {
        Estabelecimento obj = new Estabelecimento();
        BeanUtils.copyProperties(objDto, obj);
        return obj;
    }

    public static List<Estabelecimento> converterDto(List<EstabelecimentoSimplesDto> objDtos) {
        if(CollectionUtils.isEmpty(objDtos)) return new ArrayList<>();
        return objDtos.stream().map(estabelecimentoSimplesDto -> {
            return EstabelecimentoSimplesDto.converterDto(estabelecimentoSimplesDto);
        }).collect(Collectors.toList());
    }
}
