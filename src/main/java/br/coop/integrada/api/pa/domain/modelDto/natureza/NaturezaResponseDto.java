package br.coop.integrada.api.pa.domain.modelDto.natureza;

import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class NaturezaResponseDto extends AbstractResponseDto {
    private String naturezaOperacao;
    private String serie;
    private String modeloEletronico;

    public static NaturezaResponseDto construir(String naturezaOperacao, String serie, String modeloEletronico) {
        var objDto = new NaturezaResponseDto();
        objDto.setNaturezaOperacao(naturezaOperacao);
        objDto.setSerie(serie);
        objDto.setModeloEletronico(modeloEletronico);

        objDto.setIntegrated(true);
        objDto.setMessage("Natureza de operação encontrada com sucesso!");

        return objDto;
    }

    public static NaturezaResponseDto construir(Boolean integrated, String message) {
        var objDto = new NaturezaResponseDto();

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static NaturezaResponseDto construir(Boolean integrated, String message, Exception exception) {
        var objDto = new NaturezaResponseDto();

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }
}
