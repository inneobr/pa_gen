package br.coop.integrada.api.pa.domain.modelDto.recEntrega;

import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.recEntrega.SituacaoRe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SituacaoReDto {
	
	private Long id;
	
	@NotBlank(message = "Campo {codigo} obrigatório")
	private Long codigo;
	
	@NotBlank(message = "Campo {descricao} obrigatório")
	private String descricao;
	
	private IntegrationOperacaoEnum operacao;	
	
	public static SituacaoReDto construir(SituacaoRe situacaoRe) {
		SituacaoReDto objDto = new SituacaoReDto();
        BeanUtils.copyProperties(situacaoRe, objDto);
		return objDto;
	}
	
	public static List<SituacaoReDto> construir(List<SituacaoRe> situacoesRe) {
		if(CollectionUtils.isEmpty(situacoesRe)) return Collections.emptyList();
		return situacoesRe.stream().map(situacaoRe -> {
			return construir(situacaoRe);
		}).toList();
	}
}
