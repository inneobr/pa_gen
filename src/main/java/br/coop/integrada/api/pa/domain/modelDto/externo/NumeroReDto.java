package br.coop.integrada.api.pa.domain.modelDto.externo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class NumeroReDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codEstabel;
    private Long idGenesis;
    private Long nroDocto;
    
    @JsonProperty(access = Access.WRITE_ONLY)
    private String results;

    public static NumeroReDto construir(String codEstabel, Long idGenesis, Long nroDocto) {
        var objDto = new NumeroReDto();
        objDto.setCodEstabel(codEstabel);
        objDto.setIdGenesis(idGenesis);
        objDto.setNroDocto(nroDocto);
        return objDto;
    }
}
