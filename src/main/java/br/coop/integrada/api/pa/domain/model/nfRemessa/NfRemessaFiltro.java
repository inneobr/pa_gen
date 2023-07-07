package br.coop.integrada.api.pa.domain.model.nfRemessa;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.domain.enums.StatusNotaFiscalEnum;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor	
public class NfRemessaFiltro implements Serializable{		
	private static final long serialVersionUID = 1L;
	
	private StatusNotaFiscalEnum status;
	private String codEstabelecimento;
	private String nrDocto;
	private Long nrRe;	
	private Boolean pendenciasFiscais = false;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dataInicio;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dataFinal;		

}
