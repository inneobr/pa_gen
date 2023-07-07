package br.coop.integrada.api.pa.domain.modelDto.baixaCredito;

import java.util.List;

import lombok.Data;

@Data
public class BaixaCreditoDtoList {
	private List<BaixaCreditoRequestDto> baixaCredito;
}
