package br.coop.integrada.api.pa.domain.model.movimentoRe;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class MovimentoReFiltro implements Serializable{		
	private static final long serialVersionUID = 1L;
	private String codEstabelecimento;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dataInicio;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dataFinal;		

}
