package br.coop.integrada.api.pa.domain.modelDto.participante.bayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.OrigemEnum;
import br.coop.integrada.api.pa.domain.model.ParticipanteBayer;
import lombok.Data;

@Data
public class ParticipanteBayerDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String cnpj;
    private String nome;

    @JsonProperty(access = Access.READ_ONLY)
    private String usuario;

    @JsonProperty(access = Access.READ_ONLY)
    private OrigemEnum origem;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy H:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
    private Date dataInclusao;

    public static ParticipanteBayerDto construir(ParticipanteBayer obj) {
        var objDto = new ParticipanteBayerDto();
        BeanUtils.copyProperties(obj, objDto);
        objDto.setDataInclusao(obj.getDataCadastro());

        return objDto;
    }

    public static List<ParticipanteBayerDto> construir(List<ParticipanteBayer> objs) {
        if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();

        return objs.stream().map(participanteBayer -> {
            return construir(participanteBayer);
        }).toList();
    }

    public String getCnpj() {
        if(Strings.isEmpty(cnpj)) return ""; 
        return cnpj.replaceAll("[^0-9]", "");
    }
}
