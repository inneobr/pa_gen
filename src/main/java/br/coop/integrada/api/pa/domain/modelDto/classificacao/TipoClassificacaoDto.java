package br.coop.integrada.api.pa.domain.modelDto.classificacao;

import br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum;
import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class TipoClassificacaoDto implements Serializable {

    private static final long serialVersionUID = 1L;
	private Long id;
    private TipoClassificacaoEnum tipoClassificacao;
    private String tipo;
    private Boolean resultadoDesconto;
    private Boolean resultadoTaxaSecagemKg;
    private Boolean resultadoTaxaSecagemValor;
    private Boolean teorPorFaixa;
    private Boolean controlePorSafra;
    private Boolean ph;
    private Boolean phCorrigido;
    private Boolean referencia;

    public static TipoClassificacaoDto construir(TipoClassificacao obj) {
        TipoClassificacaoDto objDto = new TipoClassificacaoDto();
        BeanUtils.copyProperties(obj, objDto);
        return objDto;
    }

    public static TipoClassificacao converterDto(TipoClassificacaoDto objDto) {
        TipoClassificacao obj = new TipoClassificacao();
        BeanUtils.copyProperties(objDto, obj);
        return obj;
    }
}
