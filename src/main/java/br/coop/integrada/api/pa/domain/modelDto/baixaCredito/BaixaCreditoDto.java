package br.coop.integrada.api.pa.domain.modelDto.baixaCredito;

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
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCredito;
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCreditoMov;
import lombok.Data;

@Data
public class BaixaCreditoDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	
    private String codEstabel;
    private Long nrRe;
	
    private String idRe;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dtEmiRe;
	
	private String horaEmiteRe;
	
	private String cpfProdutor;
	
	private BigDecimal qtParaBxa;
	
	private BigDecimal sldParaBxa;
	
	private String situacao;
	
	private BigDecimal qtDpi;
	
	private Integer codEmitente;
	
	private Integer nrAgendamento;
	
	private Integer safra;
	
	private String codRegional;
	
	private Long idLote;
	
	private String bayerIdConsumo;
		
	private	String bayerBid;
	
	private BigDecimal bayerQtBaixada;
	
	private OperacaoEnum operacao;

	private Boolean logIntegrado;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;
	
	private List<BaixaCreditoMovDto> bxaCreditoMov;
	
	public static BaixaCreditoDto construir(BaixaCredito baixaCredito, BaixaCreditoMov baixaCreditoMov) {
		BaixaCreditoDto baixaCreditoDto = new BaixaCreditoDto();
			
		if(baixaCredito != null) {
			BeanUtils.copyProperties(baixaCredito, baixaCreditoDto);
			
			baixaCreditoDto.setOperacao(OperacaoEnum.WRITE);
			
			if(baixaCredito.getCodEmitente() != null) {
				baixaCreditoDto.setCodEmitente(Integer.parseInt(baixaCredito.getCodEmitente()));
			}
			
			if(baixaCredito.getNrAgendamento() != null) {
				baixaCreditoDto.setNrAgendamento(Integer.parseInt(baixaCredito.getNrAgendamento()));
			}
			
			baixaCreditoDto.setIdRe(baixaCredito.getIdRe() != null ? baixaCredito.getIdRe().toString() : null);
			baixaCreditoDto.setIdLote(baixaCredito.getIdLote() != null ? baixaCredito.getIdLote().longValue() : null);
					
			baixaCreditoDto.setBxaCreditoMov(new ArrayList<>());
			
			if(baixaCreditoMov != null) {
				BaixaCreditoMovDto baixaCreditoMovDto = BaixaCreditoMovDto.construir(baixaCreditoMov);
				baixaCreditoDto.getBxaCreditoMov().add(baixaCreditoMovDto);
			}
			
		}		
		
		return baixaCreditoDto;
	}

}
