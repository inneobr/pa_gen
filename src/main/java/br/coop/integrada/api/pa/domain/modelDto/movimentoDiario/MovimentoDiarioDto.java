package br.coop.integrada.api.pa.domain.modelDto.movimentoDiario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiario;
import lombok.Data;

@Data
public class MovimentoDiarioDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String idUnico;
	private String codEstabel;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dtMovto;
	private Boolean movtoFech;
	private IntegrationOperacaoEnum operacao;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
	
	public static MovimentoDiarioDto construir(MovimentoDiario movimento) {
    	MovimentoDiarioDto movimentoDto = new MovimentoDiarioDto();
    	movimentoDto.setIdUnico(movimento.getIdUnico());
    	movimentoDto.setCodEstabel(movimento.getCodEstabel());
    	movimentoDto.setDtMovto(movimento.getDtMovto());
    	movimentoDto.setMovtoFech(movimento.getMovtoFech());
    	return movimentoDto;
    }
	public static MovimentoDiarioDto construir(String message) {
    	MovimentoDiarioDto movimentoDto = new MovimentoDiarioDto();
    	movimentoDto.setMessage(message);
    	return movimentoDto;
    }
    
    public static List<MovimentoDiarioDto> construir(List<MovimentoDiario> objs) {
        if(objs == null) return new ArrayList<>();

        return objs.stream().map(movimento -> {
            return MovimentoDiarioDto.construir(movimento);
        }).toList();
    }
    
    public static MovimentoDiario convertDto(MovimentoDiarioDto objDto) {
		var obj = new MovimentoDiario();
		BeanUtils.copyProperties(objDto, obj);

		return obj;
	}

}
