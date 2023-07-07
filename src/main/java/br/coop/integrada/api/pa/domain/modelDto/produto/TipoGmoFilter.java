package br.coop.integrada.api.pa.domain.modelDto.produto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TipoGmoFilter implements Serializable {
	private String tipoGmo;
	private BigDecimal perDeclarada;
	private BigDecimal perTestada;
	private BigDecimal vlKit;
	private String obsRomaneio;
}
