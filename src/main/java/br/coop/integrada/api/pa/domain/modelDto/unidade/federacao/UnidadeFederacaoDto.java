package br.coop.integrada.api.pa.domain.modelDto.unidade.federacao;

import br.coop.integrada.api.pa.domain.model.UnidadeFederacao;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class UnidadeFederacaoDto implements Serializable {
    private Long id;
    private String estado;
    private String estadoNome;
    private String codigoIbge;
    private Date dataAtualizacao;
    private Boolean ativo;

    public static UnidadeFederacaoDto construir(UnidadeFederacao obj) {
        var objDto = new UnidadeFederacaoDto();
        BeanUtils.copyProperties(obj, objDto);

        return objDto;
    }

    public static List<UnidadeFederacaoDto> construir(List<UnidadeFederacao> objs) {
        if(objs == null) return new ArrayList<>();

        return objs.stream().map(unidadeFederacao -> {
            return UnidadeFederacaoDto.construir(unidadeFederacao);
        }).toList();
    }
}
