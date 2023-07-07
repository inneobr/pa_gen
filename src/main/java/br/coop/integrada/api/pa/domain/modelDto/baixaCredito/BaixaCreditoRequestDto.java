package br.coop.integrada.api.pa.domain.modelDto.baixaCredito;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BaixaCreditoRequestDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String codEstabel;
    private Long nrRe;
    private Long idRe;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dtEmiRe;
	private String horaEmiteRe;
	private String cpfProdutor;
	private BigDecimal qtParaBxa;
	private BigDecimal sldParaBxa;
	private String situacao;
	private BigDecimal qtDpi;
	private String codEmitente;
	private String nrAgendamento;
	private Integer safra;
	private Long codRegional;
	private BigDecimal idLote;
	private String bayerIdConsumo;
	private	String bayerBid;
	private BigDecimal bayerQtBaixada;
	private Boolean logIntegrado;
}
