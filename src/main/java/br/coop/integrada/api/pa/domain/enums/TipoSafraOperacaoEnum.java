package br.coop.integrada.api.pa.domain.enums;

public enum TipoSafraOperacaoEnum {
    SOMAR("+"),
    SUBTRAIR("-");
	
	private String valorEnvioErp;
	
	TipoSafraOperacaoEnum(String valorEnvioErp) {
		this.valorEnvioErp = valorEnvioErp;
	}
	
	public String getValorEnvioErp() {
        return valorEnvioErp;
    }
}
