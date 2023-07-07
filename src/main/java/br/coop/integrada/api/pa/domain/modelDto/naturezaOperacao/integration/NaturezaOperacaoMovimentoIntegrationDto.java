package br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.integration;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import lombok.Data;

@Data
public class NaturezaOperacaoMovimentoIntegrationDto {

	private String idUnico;
	private Integer codGrupo;
	private IntegrationOperacaoEnum operacao;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date data;
	
	private String hora;
	private String usuario;
	private String movimento;	
	private String observacao;
}
