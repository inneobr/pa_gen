package br.coop.integrada.api.pa.domain.modelDto.movimentoRe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.pedidoIntegracao.OperacaoEnum;
import br.coop.integrada.api.pa.domain.model.movimentoRe.MovimentoRe;
import lombok.Data;

@Data
public class MovimentoReDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String idRe;
	private String idMovRe;
	private String codEstabel;
	private Long nrRe;
	private String hora;
	private String transacao;
	private BigDecimal quantidade;
	private String usuario;
	private String observacao;
	private OperacaoEnum operacao;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date data;
	
	private Boolean logIntegrado;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
	
	public String origemRe;
	
	public static MovimentoReDto construir(MovimentoRe movimentoRe) {
		MovimentoReDto movimentoDto = new MovimentoReDto();
    	movimentoDto.setCodEstabel(movimentoRe.getCodEstabel());
    	movimentoDto.setIdRe(movimentoRe.getIdRe() != null ? movimentoRe.getIdRe().toString() : null);
    	movimentoDto.setNrRe(movimentoRe.getNrRe());
    	movimentoDto.setData(movimentoRe.getData());
    	movimentoDto.setHora(movimentoRe.getHora());
    	movimentoDto.setTransacao(movimentoRe.getTransacao());
    	movimentoDto.setQuantidade(movimentoRe.getQuantidade());
    	movimentoDto.setObservacao(movimentoRe.getObservacao());
    	movimentoDto.setUsuario(movimentoRe.getUsuario());
    	movimentoDto.setIdMovRe(movimentoRe.getIdMovRe());
    	
    	movimentoDto.setLogIntegrado(movimentoRe.getLogIntegrado());
    	return movimentoDto;
    }
	public static MovimentoReDto construir(String message) {
		MovimentoReDto movimentoDto = new MovimentoReDto();
    	movimentoDto.setMessage(message);
    	return movimentoDto;
    }
    
    public static List<MovimentoReDto> construir(List<MovimentoRe> objs) {
        if(objs == null) return new ArrayList<>();

        return objs.stream().map(movimento -> {
            return MovimentoReDto.construir(movimento);
        }).toList();
    }
    
    public static MovimentoRe convertDto(MovimentoReDto objDto) {
		var obj = new MovimentoRe();
		BeanUtils.copyProperties(objDto, obj);

		return obj;
	}

}
