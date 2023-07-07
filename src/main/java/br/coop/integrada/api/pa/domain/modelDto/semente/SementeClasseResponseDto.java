package br.coop.integrada.api.pa.domain.modelDto.semente;

import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class SementeClasseResponseDto extends AbstractResponseDto implements Serializable {

    private Long codigo;
    private String descricao;

    public static SementeClasseResponseDto construir(SementeClasse sementeClasse, Boolean integrated, String message) {
        return SementeClasseResponseDto.construir(sementeClasse, integrated, message, null);
    }

    public static SementeClasseResponseDto construir(SementeClasse sementeClasse, Boolean integrated, String message, String exception) {
        var objDto = new SementeClasseResponseDto();
        BeanUtils.copyProperties(sementeClasse, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception);

        return objDto;
    }

    public static SementeClasseResponseDto construir(SementeClasseDto sementeClasseDto, Boolean integrated, String message) {
        return SementeClasseResponseDto.construir(sementeClasseDto, integrated, message, null);
    }

    public static SementeClasseResponseDto construir(SementeClasseDto sementeClasseDto, Boolean integrated, String message, String exception) {
        var objDto = new SementeClasseResponseDto();
        BeanUtils.copyProperties(sementeClasseDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception);

        return objDto;
    }
}
