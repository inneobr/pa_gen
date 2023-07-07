package br.coop.integrada.api.pa.domain.modelDto.integration;

import java.util.ArrayList;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;
import br.coop.integrada.api.pa.domain.enums.integration.HttpRequestMethodEnum;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoFuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;

@Data
public class IntegracaoPaginaFuncionalidadeDto {
	private Long id;		
	private FuncionalidadePaginaEnum funcionalidade;
	private HttpRequestMethodEnum methodSend;
	private String endPointSend;
	private String payLoadRequestSend;
	private String payLoadResponseSend;
	private HttpRequestMethodEnum methodReturn;
	private String endPointReturn;
	private String payLoadRequestReturn;
	private String payLoadResponseReturn;
	private TimerSchedulerDto schedulerSend;
	private TimerSchedulerDto schedulerReturn;
	private SituacaoFuncionalidadePaginaEnum situacao;
	
	public static IntegracaoPaginaFuncionalidadeDto construir(IntegracaoPaginaFuncionalidade obj) {
		var objDto = new IntegracaoPaginaFuncionalidadeDto();
		BeanUtils.copyProperties(obj, objDto);
		objDto.setSchedulerSend(TimerSchedulerDto.construir(obj.getSchedulerSend()));
		objDto.setSchedulerReturn(TimerSchedulerDto.construir(obj.getSchedulerReturn()));
		return objDto;
	}
	
	public static List<IntegracaoPaginaFuncionalidadeDto> construir(List<IntegracaoPaginaFuncionalidade> objs) {
		if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();
		return objs.stream().map(funcionalidadeDto -> {
			return construir(funcionalidadeDto);
		}).toList();
	}
}
