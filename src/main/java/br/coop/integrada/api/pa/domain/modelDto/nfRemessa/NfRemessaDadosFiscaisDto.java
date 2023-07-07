package br.coop.integrada.api.pa.domain.modelDto.nfRemessa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class NfRemessaDadosFiscaisDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String pjNroNota;
	private String pjSerie;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date pjDtEmissao;
	private BigDecimal pjVlTotNota;
	private BigDecimal pjQtTotNota;
	private String pjChaveAcesso;
	private String pjNatOper;
}
