package br.coop.integrada.api.pa.domain.enums.integration;

public enum HttpRequestMethodEnum {
	POST("Post"), GET("Get"), PUT("Put"), DELETE("Delete");
    
    private final String descricao;
    
    private HttpRequestMethodEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
