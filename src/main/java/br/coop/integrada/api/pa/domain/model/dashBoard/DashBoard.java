package br.coop.integrada.api.pa.domain.model.dashBoard;

import java.math.BigDecimal;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import br.coop.integrada.api.pa.domain.modelDto.Response;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DashBoard {
	
	private BigDecimal romaneio;
	private BigDecimal pesoLiquido;
	private BigDecimal tonPesoLiquido;
	private BigDecimal scPesoLiquido;
	private BigDecimal tonRendaLiquida;
	private BigDecimal scRendaLiquida;
	private BigDecimal entradaSemTicket;
	private BigDecimal entradaComTicket;
	
}
