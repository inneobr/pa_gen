package br.coop.integrada.api.pa.domain.modelDto.recEntrega;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.domain.enums.TipoPesagemBalancaEnum;
import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.MovimentoReEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor	
public class RecEntregaFilter implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String codEstabelecimento;
	private String produto;
	private String produtor;
	private Long matriculaImovel;
	private String motorista;
	private String codigoGrupoProduto;
	private String safra;
	private Integer nroDocPesagemInicial;
	private Integer nroDocPesagemFinal;
	private String placa;
	private MovimentoReEnum status; 
	private TipoPesagemBalancaEnum tipoPesagemBalanca;
	private Long nrReInicial;
	private Long nrReFinal;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date inicio;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date termino;
	
	
}
