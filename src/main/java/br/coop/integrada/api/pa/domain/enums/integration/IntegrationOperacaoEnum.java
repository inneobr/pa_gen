package br.coop.integrada.api.pa.domain.enums.integration;

public enum IntegrationOperacaoEnum {
	WRITE, DELETE;

	public static IntegrationOperacaoEnum toEnum(Boolean ativo) {
		if(ativo == null) {
			return null;
		}

		return ativo ? WRITE : DELETE;
	}
}
