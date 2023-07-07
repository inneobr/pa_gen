package br.coop.integrada.api.pa.domain.modelDto.preco;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.preco.Preco;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PrecoSaveDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String idUnico;
    private String codigoEstabelecimento;
    private String codigoProduto;
    private String codigoReferencia;
    private BigDecimal precoFiscal;
    private BigDecimal precoFechamento;
    private BigDecimal precoFechamentoCoco;
	
	private IntegrationOperacaoEnum operacao;	
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataValidade;
    private String horaValidade;

    public static Preco converterDto(PrecoSaveDto objDto) {
        var obj = new Preco();
        BeanUtils.copyProperties(objDto, obj);

        return obj;
    }
}
