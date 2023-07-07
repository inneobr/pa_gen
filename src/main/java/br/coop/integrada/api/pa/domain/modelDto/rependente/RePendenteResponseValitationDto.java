package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RePendenteResponseValitationDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean possuiErro;
    private List<String> mensagens;

    public static RePendenteResponseValitationDto construir(List<String> mensagens) {
        var objDto = new RePendenteResponseValitationDto();

        Boolean isPossuiErro = mensagens != null && !mensagens.isEmpty();
        objDto.setPossuiErro(isPossuiErro);
        objDto.setMensagens(mensagens);

        return objDto;
    }
}
