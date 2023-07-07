package br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfRemessa;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class RequestNfRemessa {
	private String id;
	private String codEstabel;
	private String natOperacao;
	private Long nrRe;
	private String funcao;
	private String serieDocto;
	private String nrDocto;
	private String origem;
	private Integer seqItem;
	private String codRefer;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dtCriacaoPedido;
	private String hrCriacaoPedido;
	private BigDecimal quantidade;
}
