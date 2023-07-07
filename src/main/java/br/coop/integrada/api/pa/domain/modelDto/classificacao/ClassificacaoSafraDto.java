package br.coop.integrada.api.pa.domain.modelDto.classificacao;

import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoSafra;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ClassificacaoSafraDto implements Serializable {

    private Long id;

    @NotNull(message = "O campo {safra} é obrigatório")
    private Integer safra;

    public static ClassificacaoSafraDto construir(ClassificacaoSafra obj) {
        ClassificacaoSafraDto objDto = new ClassificacaoSafraDto();
        BeanUtils.copyProperties(obj, objDto);
        return objDto;
    }

    public static List<ClassificacaoSafraDto> construir(List<ClassificacaoSafra> objs) {
        return objs.stream().map(classificacaoSafra -> {
            return ClassificacaoSafraDto.construir(classificacaoSafra);
        }).collect(Collectors.toList());
    }

    public static ClassificacaoSafra converterDto(ClassificacaoSafraDto objDto, Classificacao classificacao) {
        ClassificacaoSafra obj = new ClassificacaoSafra();
        BeanUtils.copyProperties(objDto, obj);

        obj.setClassificacao(classificacao);

        return obj;
    }

    public static List<ClassificacaoSafra> converterDto(List<ClassificacaoSafraDto> objDtos, Classificacao classificacao) {
        return objDtos.stream().map(classificacaoSafraDto -> {
            return ClassificacaoSafraDto.converterDto(classificacaoSafraDto, classificacao);
        }).collect(Collectors.toList());
    }
}
