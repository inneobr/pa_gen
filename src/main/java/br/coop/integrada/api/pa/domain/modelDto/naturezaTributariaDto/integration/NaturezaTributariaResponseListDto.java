package br.coop.integrada.api.pa.domain.modelDto.naturezaTributariaDto.integration;

import java.util.List;

import lombok.Data;

import java.util.ArrayList;

@Data
public class NaturezaTributariaResponseListDto {
	private List<NaturezaTributariaResponseDto> naturezasTributarias = new ArrayList<>(); 
}
