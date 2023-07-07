package br.coop.integrada.api.pa.domain.modelDto;

import br.coop.integrada.api.pa.domain.model.HistoricoGenerico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoGenericoDto implements Serializable {
    private String username;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dataCadastro;
    private String movimento;
    private String observacao;

    public static HistoricoGenericoDto construir(HistoricoGenerico obj) {
        HistoricoGenericoDto objDto = new HistoricoGenericoDto();
        BeanUtils.copyProperties(obj, objDto);
        return objDto;
    }

    public static List<HistoricoGenericoDto> construir(List<HistoricoGenerico> objs) {
        return objs.stream()
                .map(historicoGenerico -> {
                    return HistoricoGenericoDto.construir(historicoGenerico);
                }).collect(Collectors.toList());
    }
}
