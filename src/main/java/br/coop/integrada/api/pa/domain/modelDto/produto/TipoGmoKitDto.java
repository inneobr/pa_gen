package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TipoGmoKitDto {
	private boolean pagaKit;
	private BigDecimal valorKit;
}
