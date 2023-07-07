package br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ParametroEstabelecimentoDataMovimentoResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	private Date dataMovimentoAberto;
	private Boolean status;
	private String mensagem;
	
	public static ParametroEstabelecimentoDataMovimentoResponseDto construir(Date dataMovimentoAberto) {
		var objDto = new ParametroEstabelecimentoDataMovimentoResponseDto();
		objDto.setDataMovimentoAberto(dataMovimentoAberto);
		objDto.setStatus(true);
		return objDto;
	}
	
	public static ParametroEstabelecimentoDataMovimentoResponseDto construir(String mensagem) {
		var objDto = new ParametroEstabelecimentoDataMovimentoResponseDto();
		objDto.setMensagem(mensagem);
		objDto.setStatus(false);
		return objDto;
	}
}
