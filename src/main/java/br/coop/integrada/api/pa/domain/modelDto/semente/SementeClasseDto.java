package br.coop.integrada.api.pa.domain.modelDto.semente;

import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SementeClasseDto implements Serializable {

    private Long id;

    @NotNull(message = "O campo {codigo} é obrigatório")
    private Long codigo;

    @NotNull(message = "O campo {descricao} é obrigatório")
    @NotBlank(message = "O campo {descricao} não pode ser vazio")
    @Size(message = "O campo {descricao} só pode ter no máximo 30 caracteres", max = 30)
    private String descricao;
    private Date dataAtualizacao;
    private Boolean ativo;

    public static SementeClasseDto construir(SementeClasse obj) {
        var objDto = new SementeClasseDto();
        BeanUtils.copyProperties(obj, objDto);

        if(obj.getDataAtualizacao() == null) {
            objDto.setDataAtualizacao(obj.getDataCadastro());
        }

        return objDto;
    }

    public static List<SementeClasseDto> construir(List<SementeClasse> objs) {
        if(objs == null) return new ArrayList<>();

        return objs.stream().map(sementeClasse -> {
            return SementeClasseDto.construir(sementeClasse);
        }).toList();
    }
}
