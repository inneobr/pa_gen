package br.coop.integrada.api.pa.domain.modelDto.movimentoPesagem;

import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class MovtoPesagemDto {
	private String codEstabel;
    private Integer safra;
    private Integer nroDocPesagem;
    private Integer nroMovto;  	
    private String idMovtoPesagem;
    private String serieDocto;
    private String nroDocto;
    private Integer codEmitente;
    private String natOperacao;
	private BigDecimal pesoMovto;
	private BigDecimal rendaMovto;
    private String operacao;
    private String movto;
    private boolean estornado;
    private Integer nrRe;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dtMovto;
}
