package br.coop.integrada.api.pa.domain.modelDto.integration;

import java.util.ArrayList;
import java.util.List;

import br.coop.integrada.api.pa.domain.enums.integration.HttpRequestMethodEnum;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;
import lombok.Data;

@Data
public class IntegracaoFuncionalidadeTeste {
	private Boolean tipoSend;
	private HttpRequestMethodEnum methodSend;
	private HttpRequestMethodEnum methodReturn;
	
	private String urlApiDev;
	private String urlApiProd;
	private String urlApiHomolog;
	
	private String ambiente;
	private Long idIntegrationAuth;
	
	private String endPointSend;
	private String endPointReturn;
	
	private String payLoadRequestSend;		
	private String payLoadRequestReturn;
	
	private List<IntegracaoPaginaHeader> paginaHeader = new ArrayList<>();
	
	public String getUrlPrincipalApi() {
		switch (this.ambiente) {
			case "dev": {
				return getUrlApiDev();
			}
			case "prod": {
				return getUrlApiProd();
			}
			case "homolog": {
				return getUrlApiHomolog();
			}
			default:
				return null;
		}
	}
}
