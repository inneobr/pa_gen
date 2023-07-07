package br.coop.integrada.api.pa.domain.modelDto.integration;

import java.util.List;
import org.springframework.beans.BeanUtils;
import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum;
import br.coop.integrada.api.pa.domain.enums.integration.TipoIntegracaoEnum;
import br.coop.integrada.api.pa.domain.enums.integration.TipoSincronizacaoEnum;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;
import lombok.Data;

@Data
public class IntegracaoPaginaDto {	
	private Long id;	
	private PaginaEnum paginaEnum;
	private IntegrationAuth authProd;
	private IntegrationAuth authDev;
	private IntegrationAuth authHomolog;
	private String urlApiProd;
	private String urlApiDev;
	private String urlApiHomolog;
	private OrigemInputEnum origenEnum; 
	private TipoIntegracaoEnum tipoEnum;
	private TipoSincronizacaoEnum sincronizacaoEnum;	
	private List<IntegracaoPaginaHeaderDto> paginaHeader;
	private List<IntegracaoPaginaFuncionalidadeDto> funcionalidades;
	private List<IntegracaoPaginaParametroDto> paginaParametros;
	

	public IntegracaoPaginaFuncionalidadeDto getFuncionalidade(FuncionalidadePaginaEnum funcionalidade) {
		for(IntegracaoPaginaFuncionalidadeDto item: funcionalidades) {
			if(item.getFuncionalidade().equals(funcionalidade)) {
				return item;
			}
		}
		return null;
	}
		
	public static IntegracaoPaginaDto construir(IntegracaoPagina obj) {
		var objDto = new IntegracaoPaginaDto();
		BeanUtils.copyProperties(obj, objDto);
		
		List<IntegracaoPaginaFuncionalidadeDto> funcionalidades = IntegracaoPaginaFuncionalidadeDto.construir(obj.getFuncionalidades());
		objDto.setFuncionalidades(funcionalidades);
		
		List<IntegracaoPaginaHeaderDto> headers = IntegracaoPaginaHeaderDto.construir(obj.getHeaders());
		objDto.setPaginaHeader(headers);
		
		List<IntegracaoPaginaParametroDto> parametros = IntegracaoPaginaParametroDto.construir(obj.getParametros());
		objDto.setPaginaParametros(parametros);
		
		return objDto;
	}
}
