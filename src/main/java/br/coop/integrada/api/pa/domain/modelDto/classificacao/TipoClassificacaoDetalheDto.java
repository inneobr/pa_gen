package br.coop.integrada.api.pa.domain.modelDto.classificacao;

import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class TipoClassificacaoDetalheDto implements Serializable {

    private Long id;
    private Boolean resultadoDesconto;
    private Boolean resultadoTaxaSecagemKg;
    private Boolean resultadoTaxaSecagemValor;
    private Boolean teorPorFaixa;
    private Boolean controlePorSafa;
    private Boolean ph;
    private Boolean phCorrigido;
    private Boolean referencia;
    private List<ClassificacaoDetalheDto> detalhes;

    public static TipoClassificacaoDetalheDto construir(Classificacao obj) {
        TipoClassificacaoDetalheDto objDto = new TipoClassificacaoDetalheDto();

        TipoClassificacao tipoClassificacao = obj.getTipoClassificacao();
        BeanUtils.copyProperties(tipoClassificacao, objDto);

        List<ClassificacaoDetalheDto> detalhes = ClassificacaoDetalheDto.construir(obj.getDetalhes());
        objDto.setDetalhes(detalhes);

        return objDto;
    }
}
