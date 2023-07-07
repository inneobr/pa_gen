package br.coop.integrada.api.pa.domain.modelDto.baixaCredito;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.pedidoIntegracao.OperacaoEnum;
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCreditoMov;
import lombok.Data;

@Data
public class BaixaCreditoMovDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String codEstabel;
    private Long nrRe;
    private String idRe;	
    private String idMovtoBxaCred;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dtMovto;
	
    private String hrMovto;
	
    private String codUsuario;
	
    private String transacao;
	
    private String observacao;
	
    private BigDecimal idTransacao;
	
    private String codAutenticTransacao;
	
    private BigDecimal qtdade;
    
	private Boolean logIntegrado;
    
	private OperacaoEnum operacao;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;

	public String getOrigemRe() {
		if(idMovtoBxaCred != null) {
			String origem = idMovtoBxaCred.substring(0, 3);
			
			if(origem.equalsIgnoreCase("DTS")) {
				return "DATASUL";
			}
		}
		
		return "GENESIS";
	}
	
	public static BaixaCreditoMovDto construir(BaixaCreditoMov baixaCreditoMov) {
		BaixaCreditoMovDto creditoMovDto = new BaixaCreditoMovDto();
		BeanUtils.copyProperties(baixaCreditoMov, creditoMovDto);
		
		creditoMovDto.setOperacao(OperacaoEnum.WRITE);
		creditoMovDto.setIdRe(baixaCreditoMov.getIdRe() != null ? baixaCreditoMov.getIdRe().toString() : null);
		return creditoMovDto;
	}

}
