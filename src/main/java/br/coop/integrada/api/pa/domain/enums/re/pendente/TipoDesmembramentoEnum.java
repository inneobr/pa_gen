package br.coop.integrada.api.pa.domain.enums.re.pendente;

import java.util.List;

import br.coop.integrada.api.pa.domain.modelDto.rependente.TipoDesmembramentoEnumDto;

public enum TipoDesmembramentoEnum {
	PESO_LIQUIDO ("Peso Líquido (Peso bruto - Tara)"),
	RENDA_LIQUIDA ("Renda Líquida (Peso liquido - Classificação)");

	private String descricao;
	
	TipoDesmembramentoEnum(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public static List<TipoDesmembramentoEnumDto> listar() {
		TipoDesmembramentoEnum[] tipos = TipoDesmembramentoEnum.values();
    	return TipoDesmembramentoEnumDto.construir(tipos);
    }
}
